package tech.eazley.PharmaReconile.Controllers;

import org.apache.pdfbox.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tech.eazley.PharmaReconile.Models.*;
import tech.eazley.PharmaReconile.Models.Http.ReconciliationResponse;
import tech.eazley.PharmaReconile.Services.ReconciliationService;
import tech.eazley.PharmaReconile.Services.PDFFileService;
import tech.eazley.PharmaReconile.Services.PDFService;
import tech.eazley.PharmaReconile.Services.PharmacyMemberService;
import tech.eazley.PharmaReconile.Util.ConverterUtil;
import tech.eazley.PharmaReconile.Util.ObjectMapperUtil;
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
    private ReconciliationService reconciliationService;

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
    public List<Reconciliation.PDFCacheProjection> getAllReconciliations(Authentication authentication)
    {
        PharmacyMember pharmacyMember = getPharmacyMember(authentication);
        return reconciliationService.getAllCachesByPharmacy(pharmacyMember.getPharmacy());
    }


    // Create a reconciliation for sagicor
    @PostMapping("/sagicor")
    public ReconciliationResponse sagicor(@RequestParam String vendor,
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

        //PDFFile sagicorPDFFile = new PDFFile();
        //PDFFile clientFile = new PDFFile();
        //sagicorPDFFile.setData(sagicorData);
        //clientFile.setData(clientData);

        //clientFile.setFileType("client-data");
        //sagicorPDFFile.setFileType("sagicor-data");

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        // Create a PDF Cache row to represent the cache of both the client and sagicor file
        Reconciliation reconciliation = new Reconciliation();
        reconciliation.setDateAdded(timestamp.getTime());
        reconciliation.setToPeriod(toPeriodTimestamp);
        reconciliation.setFromPeriod(fromPeriodTimestamp);

        // Get pharmacy member to set pharmacy in the cache to be fetched later
        PharmacyMember pharmacyMember = getPharmacyMember(authentication);

        reconciliation.setPharmacy(pharmacyMember.getPharmacy());
        reconciliation.setVendor(ConverterUtil.stringToVendor(vendor));
        reconciliation.setProvider(Provider.SAGICOR);

        // Save this case to the db
        reconciliationService.saveCache(reconciliation);

        // Set the parent table / object
        //clientFile.setPdfCache(pdfCache);
        //sagicorPDFFile.setPdfCache(pdfCache);

        // Save the files to db for caching
        //pdfFileService.saveFile(sagicorPDFFile);
        //pdfFileService.saveFile(clientFile);

        // Set the needed data
        pdfService.setClientData(clientData);
        pdfService.setSagicorData(sagicorData);

        // Add up the payable and charged here
        ArrayList<DrugClaim> claimResponseBodies = new ArrayList<>();
        float claimsTotals = pdfService.getSagicorClaimTotals();


        if (vendor.equals("pharmacy-works"))
        {
            float totalCharged = 0;
            float totalPayable = 0;
            claimResponseBodies = pdfService.extractPharmacyWorksClaims();

            for (DrugClaim drug: claimResponseBodies) {
                totalCharged += drug.getCharged();
                totalPayable += drug.getPayable();
            }

            reconciliation.setCharged(totalCharged);
            reconciliation.setPayable(totalPayable);
            reconciliation.setSagicorTotals(claimsTotals);

            String drugClaimsToJson = ObjectMapperUtil.drugClaimsToJson(claimResponseBodies);

            reconciliation.setReconciliationDetails(drugClaimsToJson);
            reconciliationService.saveCache(reconciliation);
        }

        return new ReconciliationResponse(claimResponseBodies,claimsTotals);
    }

    // TODO : Get Highlight for different vendors
    @GetMapping(value = "/sagicor")
    private void getHighlight(
                          HttpServletResponse response, Authentication authentication) {

        PharmacyMember pharmacyMember = getPharmacyMember(authentication);

        Reconciliation fileCache = reconciliationService.getLatestCache(pharmacyMember.getPharmacy());
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
    List<Reconciliation.PDFCacheProjection> getSagicorReconciliations(Authentication authentication)
    {
        System.out.println("Hello World");
        PharmacyMember pharmacyMember = getPharmacyMember(authentication);
        return reconciliationService.getAllCachesByPharmacyAndProvider(pharmacyMember.getPharmacy(),Provider.SAGICOR);
    }


    // TODO : Get Type of vendor and return the data based on that
    @GetMapping("/sagicor/cache/{id}")
    public List<DrugClaim> getReconciliation(@PathVariable int id, Authentication authentication)
    {
        Reconciliation cache = reconciliationService.getCacheByID(id);

        List<PDFFile> clientFiles = pdfFileService.getByPDFCacheAndType(cache,"client-data");
        List<PDFFile> sagicorFiles = pdfFileService.getByPDFCacheAndType(cache,"sagicor-data");
        pdfService.setClientData(clientFiles.get(0).getData());
        pdfService.setSagicorData(sagicorFiles.get(0).getData());

        return pdfService.extractPharmacyWorksClaims();
    }
}
