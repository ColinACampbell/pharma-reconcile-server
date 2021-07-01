package tech.eazley.PharmaReconile.Repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.eazley.PharmaReconile.Models.PDFCache;
import tech.eazley.PharmaReconile.Models.Pharmacy;

import javax.transaction.Transactional;

@Repository
public interface PDFCacheRepo extends CrudRepository<PDFCache,Integer> {
    @Query(
            nativeQuery = true,
            value = "select 1 from pdfcache where pharmacy_id = ?1 ORDER BY id DESC LIMIT 1"
    )
    PDFCache findLatestCache(int pharmacyID);
}

