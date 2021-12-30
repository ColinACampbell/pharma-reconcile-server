package tech.eazley.PharmaReconile.Repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.eazley.PharmaReconile.Models.Reconciliation;

import java.util.List;

@Repository
public interface ReconciliationRepository extends CrudRepository<Reconciliation,Integer> {
    @Query(
            nativeQuery = true,
            value = "select * from pdfcache where pdfcache.pharmacy_id = ?1 ORDER BY reconciliations.id DESC LIMIT 1"
    )
    Reconciliation findLatestCache(int pharmacyID);

    @Query(
            nativeQuery = true,
            value = "select id, from_period as fromPeriod, to_Period as toPeriod, date_added as dateAdded, sagicor_totals as sagicorTotals, provider, charged, payable from reconciliations where reconciliations.pharmacy_id = ?1 and provider = ?2"
    )
    List<Reconciliation.PDFCacheProjection> findAllByPharmacyAndProvider(int pharmacyID, String provider);

    @Query(
            nativeQuery = true,
            value = "select id, from_period as fromPeriod, to_Period as toPeriod, date_added as dateAdded, sagicor_totals as sagicorTotals, provider, charged, payable from reconciliations where reconciliations.pharmacy_id = ?1"
    )
    List<Reconciliation.PDFCacheProjection> findAllByPharmacy(int pharmacyID);


    @Query(
            nativeQuery = true,
            value = "select id, from_period as fromPeriod, to_Period as toPeriod, date_added as dateAdded, sagicor_totals as sagicorTotals, provider, charged, payable from reconciliations where id = ?1"
    )
    Reconciliation.PDFCacheProjection findCacheProjectionByID(int id);
}

