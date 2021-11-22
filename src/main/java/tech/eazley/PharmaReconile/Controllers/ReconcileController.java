package tech.eazley.PharmaReconile.Controllers;

import org.apache.pdfbox.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tech.eazley.PharmaReconile.Models.*;
import tech.eazley.PharmaReconile.Services.PDFCacheService;
import tech.eazley.PharmaReconile.Services.PDFFileService;
import tech.eazley.PharmaReconile.Services.PDFService;
import tech.eazley.PharmaReconile.Services.PharmacyMemberService;
import tech.eazley.PharmaReconile.Util.ConverterUtil;
import tech.eazley.PharmaReconile.Utils.PDFAnnotator;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Timestamp;
import java.util.*;

@RestController
@RequestMapping("api/reconciliation")
@CrossOrigin( origins = "http://localhost:3000/", allowCredentials = "true")
public class ReconcileController {

    @Autowired
    private PDFService pdfService;

    @Autowired
    private PDFAnnotator pdfAnnotator;
    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    private PDFCacheService pdfCacheService;

    @Autowired
     PDFFileService pdfFileService;

    @Autowired
    PharmacyMemberService pharmacyMemberService;

    private PharmacyMember getPharmacyMember(Authentication authentication)
    {
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        return pharmacyMemberService.findByUser(userDetails.getUser());
    }

    @GetMapping("/")
    public List<PDFCache.PDFCacheProjection> getAllReconciliations(Authentication authentication)
    {
        PharmacyMember pharmacyMember = getPharmacyMember(authentication);
        return pdfCacheService.getAllCachesByPharmacy(pharmacyMember.getPharmacy());
    }


    @PostMapping("/sagicor")
    public ArrayList<DrugClaimResponseBody> sagicor(@RequestParam String vendor,
                                                            @RequestBody HashMap<String,Object> body,
                                                            Authentication authentication)
    {

        String client = (String) body.get("client");
        String sagicor = (String) body.get("sagicor");

        // Add from and to period
        long fromPeriodTimestamp =  (Long) body.get("fromPeriodTimeStamp");
        long toPeriodTimestamp = (Long) body.get("toPeriodTimeStamp");

        // Get pure base64
        String clientBase64 = client.split(",")[1];

        String sagicorBase64 = sagicor.split(",")[1];
        // Encode base64 to bytes
        byte[] clientData = Base64.getDecoder().decode(clientBase64);
        byte[] sagicorData = Base64.getDecoder().decode(sagicorBase64);
        PDFFile sagicorPDFFile = new PDFFile();
        PDFFile clientFile = new PDFFile();
        sagicorPDFFile.setData(sagicorData);
        clientFile.setData(clientData);

        clientFile.setFileType("client-data");
        sagicorPDFFile.setFileType("sagicor-data");

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        // Create a PDF Cache row to represent the cache of both the client and sagicor file
        PDFCache pdfCache = new PDFCache();
        pdfCache.setDateAdded(timestamp.getTime());
        pdfCache.setToPeriod(toPeriodTimestamp);
        pdfCache.setFromPeriod(fromPeriodTimestamp);

        // Get pharmacy member to set pharmacy in the cache to be fetched later
        PharmacyMember pharmacyMember = getPharmacyMember(authentication);

        pdfCache.setPharmacy(pharmacyMember.getPharmacy());
        pdfCache.setVendor(ConverterUtil.stringToVendor(vendor));
        pdfCache.setProvider(Provider.SAGICOR);

        // Save this case to the db
        pdfCacheService.saveCache(pdfCache);

        // Set the parent table / object
        clientFile.setPdfCache(pdfCache);
        sagicorPDFFile.setPdfCache(pdfCache);

        // Save the files to db for caching
        pdfFileService.saveFile(sagicorPDFFile);
        pdfFileService.saveFile(clientFile);

        // Set the needed data
        pdfService.setClientData(clientData);
        pdfService.setSagicorData(sagicorData);

        // Add up the payable and charged here
        ArrayList<DrugClaimResponseBody> claimResponseBodies = new ArrayList<>();
        if (vendor.equals("pharmacy-works"))
        {
            double totalCharged = 0;
            double totalPayable = 0;
            claimResponseBodies = pdfService.extractPharmacyWorksClaims();

            for (DrugClaimResponseBody drug: claimResponseBodies) {
                totalCharged += drug.getCharged();
                totalPayable += drug.getPayable();
            }

            pdfCache.setCharged(totalCharged);
            pdfCache.setPayable(totalPayable);
            pdfCache.setSagicorTotals(pdfService.getSagicorClaimTotals());
            pdfCacheService.saveCache(pdfCache);
        }

        return claimResponseBodies;
    }

    // TODO : Get Highlight for different vendors
    @GetMapping(value = "/sagicor")
    private void getHighlight(
                          HttpServletResponse response, Authentication authentication) {

        PharmacyMember pharmacyMember = getPharmacyMember(authentication);

        PDFCache fileCache = pdfCacheService.getLatestCache(pharmacyMember.getPharmacy());
        List<PDFFile> clientFiles = pdfFileService.getByPDFCacheAndType(fileCache,"client-data");
        List<PDFFile> sagicorFiles = pdfFileService.getByPDFCacheAndType(fileCache,"sagicor-data");

        System.out.println("Sagicor Data Length "+sagicorFiles.get(0).getData().length);
        System.out.println("Client Data Length "+clientFiles.get(0).getData().length);

        pdfService.setSagicorData(sagicorFiles.get(0).getData());
        pdfService.setClientData(clientFiles.get(0).getData());

        byte[] data = pdfService.highlightReferences(pdfService.extractPharmacyWorksClaims());
        System.out.println("Result data length "+data.length);
        try {
            // get your file as InputStream
            InputStream is = new ByteArrayInputStream(data);
            // copy it to response's OutputStream

            response.setHeader("Content-Disposition", "attachment; filename=demo.pdf");
            response.setContentType("application/pdf");
            IOUtils.copy(is,response.getOutputStream());
            //org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());

            response.flushBuffer();
        } catch (IOException ex) {
            //log.info("Error writing file to output stream. Filename was '{}'", fileName, ex);
            throw new RuntimeException("IOError writing file to output stream");
        }

    }

    @GetMapping("/sagicor/caches")
    List<PDFCache.PDFCacheProjection> getSagicorReconciliations(Authentication authentication)
    {
        PharmacyMember pharmacyMember = getPharmacyMember(authentication);
        return pdfCacheService.getAllCachesByPharmacyAndProvider(pharmacyMember.getPharmacy(),Provider.SAGICOR);
    }


    // TODO : Get Type of vendor and return the data based on that
    @GetMapping("/sagicor/cache/{id}")
    public List<DrugClaimResponseBody> getPDFCache(@PathVariable int id, Authentication authentication)
    {
        PDFCache cache = pdfCacheService.getCacheByID(id);

        List<PDFFile> clientFiles = pdfFileService.getByPDFCacheAndType(cache,"client-data");
        List<PDFFile> sagicorFiles = pdfFileService.getByPDFCacheAndType(cache,"sagicor-data");
        pdfService.setClientData(clientFiles.get(0).getData());
        pdfService.setSagicorData(sagicorFiles.get(0).getData());

        return pdfService.extractPharmacyWorksClaims();
    }
}
