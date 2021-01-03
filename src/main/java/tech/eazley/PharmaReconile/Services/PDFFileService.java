package tech.eazley.PharmaReconile.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.eazley.PharmaReconile.Models.PDFCache;
import tech.eazley.PharmaReconile.Models.PDFFile;
import tech.eazley.PharmaReconile.Repositories.PDFFileRepo;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class PDFFileService {
    @Autowired
    private PDFFileRepo pdfFileRepo;

    @Transactional
    public List<PDFFile> getByPDFCacheAndType(PDFCache pdfCache, String type)
    {
        return pdfFileRepo.findByPdfCacheAndFileType(pdfCache,type);
    }

    public void saveFile(PDFFile file)
    {
        pdfFileRepo.save(file);
    }
}
