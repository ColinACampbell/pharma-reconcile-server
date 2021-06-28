package tech.eazley.PharmaReconile.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.eazley.PharmaReconile.Models.CreditAccount;
import tech.eazley.PharmaReconile.Models.Pharmacy;

@Repository
public interface CreditRepository extends CrudRepository<CreditAccount,Integer> {
    CreditAccount findByPharmacy(Pharmacy pharmacy);
}
