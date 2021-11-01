package tech.eazley.PharmaReconile.Repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.eazley.PharmaReconile.Models.PDFCache;
import tech.eazley.PharmaReconile.Models.Provider;

import java.util.List;

@Repository
public interface PDFCacheRepo extends CrudRepository<PDFCache,Integer> {
    @Query(
            nativeQuery = true,
            value = "select * from pdfcache where pdfcache.pharmacy_id = ?1 ORDER BY pdfcache.id DESC LIMIT 1"
    )
    PDFCache findLatestCache(int pharmacyID);

    @Query(
            nativeQuery = true,
            value = "select id, from_period as fromPeriod, to_Period as toPeriod, date_added as dateAdded, provider, charged, payable from pdfcache where pdfcache.pharmacy_id = ?1 and provider = ?2"
    )
    List<PDFCache.PDFCacheProjection> findAllByPharmacyAndProvider(int pharmacyID, String provider);

    @Query(
            nativeQuery = true,
            value = "select id, from_period as fromPeriod, to_Period as toPeriod, date_added as dateAdded, provider, charged, payable from pdfcache where pdfcache.pharmacy_id = ?1"
    )
    List<PDFCache.PDFCacheProjection> findAllByPharmacy(int pharmacyID);

    PDFCache findById(int id);
}

