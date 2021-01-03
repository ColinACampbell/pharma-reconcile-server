package tech.eazley.PharmaReconile.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.eazley.PharmaReconile.Models.PDFCache;
import tech.eazley.PharmaReconile.Repositories.PDFCacheRepo;

import javax.transaction.Transactional;

@Service
public class PDFCacheService {
    @Autowired
    private PDFCacheRepo pdfCacheRepo;

    // Get latest cache added
    public PDFCache getLatestCache()
    {
        return pdfCacheRepo.findTopByOrderByIdDesc();
    }

    // Save cache to the db
    public void saveCache(PDFCache pdfCache)
    {
        pdfCacheRepo.save(pdfCache);
    }

}
