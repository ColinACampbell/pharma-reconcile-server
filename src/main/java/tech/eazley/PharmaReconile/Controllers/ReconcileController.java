package tech.eazley.PharmaReconile.Controllers;

import org.apache.pdfbox.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;
import tech.eazley.PharmaReconile.Models.DrugClaimResponseBody;
import tech.eazley.PharmaReconile.Models.PDFCache;
import tech.eazley.PharmaReconile.Models.PDFFile;
import tech.eazley.PharmaReconile.Services.PDFCacheService;
import tech.eazley.PharmaReconile.Services.PDFFileService;
import tech.eazley.PharmaReconile.Services.PDFService;
import tech.eazley.PharmaReconile.Utils.PDFAnnotator;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.sql.Timestamp;
import java.util.*;


@RestController
@RequestMapping("api/reconciliation-service")
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

    @ResponseBody
    @GetMapping("/test")
    public ArrayList<DrugClaimResponseBody> test()
    {
        return pdfService.extractData();
    }

    // TODO Figure this out later
    @PostMapping("/image/upload")
    public ArrayList<DrugClaimResponseBody> uploadDocuments(HttpSession session, @RequestBody HashMap<String,Object> body)
    {
        String client = (String) body.get("client");
        String sagicor = (String) body.get("sagicor");

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

        PDFCache pdfCache = new PDFCache();
        pdfCache.setDataAdded(timestamp.getTime());

        // Save this case to the db
        pdfCacheService.saveCache(pdfCache);

        // Set the parent table / object
        clientFile.setPdfCache(pdfCache);
        sagicorPDFFile.setPdfCache(pdfCache);

        // Save the files to db
        pdfFileService.saveFile(sagicorPDFFile);
        pdfFileService.saveFile(clientFile);

        // Get the needed data
        pdfService.setClientData(clientData);
        pdfService.setSagicorData(sagicorData);
        return pdfService.extractData();
    }

    @GetMapping(value = "/get-highlight/{fileName}.pdf")
    private void getImage(@PathVariable String fileName,
                          HttpServletResponse response, HttpSession httpSession) throws IOException {


        PDFCache fileCache = pdfCacheService.getLatestCache();
        List<PDFFile> clientFiles = pdfFileService.getByPDFCacheAndType(fileCache,"client-data");
        List<PDFFile> sagicorFiles = pdfFileService.getByPDFCacheAndType(fileCache,"sagicor-data");

        System.out.println("Sagicor Data Length "+sagicorFiles.get(0).getData().length);
        System.out.println("Client Data Length "+clientFiles.get(0).getData().length);

        pdfService.setSagicorData(sagicorFiles.get(0).getData());
        pdfService.setClientData(clientFiles.get(0).getData());

        byte[] data = pdfService.highlightReferences(pdfService.extractData());
        System.out.println("Result data length "+data.length);
        try {
            // get your file as InputStream
            InputStream is = new ByteArrayInputStream(data);
            // copy it to response's OutputStream
            response.setHeader("Content-Disposition", "attachment; filename=\"somefile.pdf\"");
            response.setContentType("application/pdf");
            IOUtils.copy(is,response.getOutputStream());
            //org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());

            response.flushBuffer();
        } catch (IOException ex) {
            //log.info("Error writing file to output stream. Filename was '{}'", fileName, ex);
            throw new RuntimeException("IOError writing file to output stream");
        }

    }
}
