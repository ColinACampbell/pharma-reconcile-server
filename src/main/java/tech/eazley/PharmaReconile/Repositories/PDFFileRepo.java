package tech.eazley.PharmaReconile.Repositories;

import org.springframework.data.repository.CrudRepository;
import tech.eazley.PharmaReconile.Models.PDFCache;
import tech.eazley.PharmaReconile.Models.PDFFile;

import javax.transaction.Transactional;
import java.util.List;

public interface PDFFileRepo extends CrudRepository<PDFFile,Integer> {
    @Transactional
    List<PDFFile> findByPdfCacheAndFileType(PDFCache cache, String type);
}
