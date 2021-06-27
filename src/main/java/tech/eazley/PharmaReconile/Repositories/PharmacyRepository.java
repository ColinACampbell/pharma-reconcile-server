package tech.eazley.PharmaReconile.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.eazley.PharmaReconile.Models.Pharmacy;
import tech.eazley.PharmaReconile.Models.PharmacyMember;
import tech.eazley.PharmaReconile.Models.User;

@Repository
public interface PharmacyRepository extends CrudRepository<Pharmacy,Integer> {
}
