package tech.eazley.PharmaReconile.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.eazley.PharmaReconile.Models.Reconciliation;
import tech.eazley.PharmaReconile.Models.Pharmacy;
import tech.eazley.PharmaReconile.Models.Provider;
import tech.eazley.PharmaReconile.Repositories.ReconciliationRepository;
import tech.eazley.PharmaReconile.Util.ConverterUtil;

import java.util.List;

@Service
public class ReconciliationService {
    @Autowired
    private ReconciliationRepository reconciliationRepository;

    // Get latest cache added
    public Reconciliation getLatestCache(Pharmacy pharmacy)
    {
        return reconciliationRepository.findLatestCache(pharmacy.getId());
    }

    public Reconciliation.PDFCacheProjection getCacheByID(int cacheID)
    {
        return reconciliationRepository.findCacheProjectionByID(cacheID);
    }

    public List<Reconciliation.PDFCacheProjection> getAllCachesByPharmacyAndProvider(Pharmacy pharmacy, Provider provider)
    {
        return reconciliationRepository.findAllByPharmacyAndProvider(pharmacy.getId(), ConverterUtil.providerToString(provider));
    }

    public List<Reconciliation.PDFCacheProjection> getAllCachesByPharmacy(Pharmacy pharmacy)
    {
        return reconciliationRepository.findAllByPharmacy(pharmacy.getId());
    }

    // Save cache to the db
    public void saveCache(Reconciliation sagicorReconciliation)
    {
        reconciliationRepository.save(sagicorReconciliation);
    }

}
