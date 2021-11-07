package tech.eazley.PharmaReconile.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.eazley.PharmaReconile.Models.PDFCache;
import tech.eazley.PharmaReconile.Models.Pharmacy;
import tech.eazley.PharmaReconile.Models.Provider;
import tech.eazley.PharmaReconile.Repositories.PDFCacheRepo;
import tech.eazley.PharmaReconile.Util.ConverterUtil;

import java.util.List;

@Service
public class PDFCacheService {
    @Autowired
    private PDFCacheRepo pdfCacheRepo;

    // Get latest cache added
    public PDFCache getLatestCache(Pharmacy pharmacy)
    {
        return pdfCacheRepo.findLatestCache(pharmacy.getId());
    }

    public PDFCache getCacheByID(int cacheID)
    {
        return pdfCacheRepo.findById(cacheID);
    }

    public List<PDFCache.PDFCacheProjection> getAllCachesByPharmacyAndProvider(Pharmacy pharmacy, Provider provider)
    {
        return pdfCacheRepo.findAllByPharmacyAndProvider(pharmacy.getId(), ConverterUtil.providerToString(provider));
    }

    public List<PDFCache.PDFCacheProjection> getAllCachesByPharmacy(Pharmacy pharmacy)
    {
        return pdfCacheRepo.findAllByPharmacy(pharmacy.getId());
    }

    // Save cache to the db
    public void saveCache(PDFCache sagicorPdfCache)
    {
        pdfCacheRepo.save(sagicorPdfCache);
    }

}
