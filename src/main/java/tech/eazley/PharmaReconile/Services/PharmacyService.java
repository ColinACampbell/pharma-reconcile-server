package tech.eazley.PharmaReconile.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.eazley.PharmaReconile.Models.Pharmacy;
import tech.eazley.PharmaReconile.Models.User;
import tech.eazley.PharmaReconile.Repositories.PharmacyRepository;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class PharmacyService {
    @Autowired
    private PharmacyRepository pharmacyRepository;

    public void savePharmacy(Pharmacy pharmacy)
    {
        pharmacyRepository.save(pharmacy);
    }

    public List<Pharmacy> getPharmacies()
    {
        return (List<Pharmacy>) pharmacyRepository.findAll();
    }

    public Pharmacy getPharmacy(int id)
    {
        return pharmacyRepository.findById(id);
    }

    public Pharmacy updatePharmacy(int id, Pharmacy newPharmacyInfo)
    {
        Pharmacy pharmacy = this.getPharmacy(id);
        pharmacy.setPharmacyName(newPharmacyInfo.getPharmacyName());
        pharmacy.setAddress(newPharmacyInfo.getAddress());
        pharmacy.setParish(newPharmacyInfo.getParish());
        pharmacy.setIsEnabled(newPharmacyInfo.getIsEnabled());
        pharmacy.setPhone1(newPharmacyInfo.getPhone1());
        pharmacy.setPhone2(newPharmacyInfo.getPhone2());
        pharmacy.setLastPaymentDate(newPharmacyInfo.getLastPaymentDate());
        pharmacy.setNextPaymentDate(newPharmacyInfo.getLastPaymentDate() + TimeUnit.DAYS.toSeconds(newPharmacyInfo.getPaymentPeriodDays()));
        pharmacy.setPaymentPeriodDays(newPharmacyInfo.getPaymentPeriodDays());
        pharmacy.setAdminPharmacy(newPharmacyInfo.isAdminPharmacy() != null && newPharmacyInfo.isAdminPharmacy());

        pharmacyRepository.save(pharmacy);

        return pharmacy;
    }
}
