package tech.eazley.PharmaReconile.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.eazley.PharmaReconile.Models.Pharmacy;
import tech.eazley.PharmaReconile.Models.User;
import tech.eazley.PharmaReconile.Repositories.PharmacyRepository;

@Service
public class PharmacyService {
    @Autowired
    private PharmacyRepository pharmacyRepository;

    public void savePharmacy(Pharmacy pharmacy)
    {
        pharmacyRepository.save(pharmacy);
    }

    public Pharmacy findByUser(User user)
    {
        return pharmacyRepository.findByUser(user);
    }
}
