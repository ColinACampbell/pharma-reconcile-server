package tech.eazley.PharmaReconile.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.eazley.PharmaReconile.Models.PDFCache;

import javax.transaction.Transactional;

@Repository
public interface PDFCacheRepo extends CrudRepository<PDFCache,Integer> {
    PDFCache findTopByOrderByIdDesc();
}

