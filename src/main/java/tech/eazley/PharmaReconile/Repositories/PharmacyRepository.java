package tech.eazley.PharmaReconile.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.eazley.PharmaReconile.Models.Pharmacy;

@Repository
public interface PharmacyRepository extends CrudRepository<Pharmacy,Integer> {

}
