package tech.eazley.PharmaReconile.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import tech.eazley.PharmaReconile.Models.Pharmacy;
import tech.eazley.PharmaReconile.Services.PharmacyService;

import java.util.Map;

@RequestMapping("/api/pharmacy")
public class PharmacyController {

    @Autowired
    PharmacyService pharmacyService;

    @PostMapping("/register-pharmacy")
    public ResponseEntity<?> registerPharmacy(@RequestBody Map<String,String> postBody)
    {
        String pharmacyName = postBody.get("pharmacyName");
        String pharmacyAddress = postBody.get("pharmacyAddress");
        int numberOfEmployees = Integer.parseInt(postBody.get("numberOfEmployees"));

        Pharmacy pharmacy = new Pharmacy();
        pharmacy.setPharmacyName(pharmacyName);
        pharmacy.setAddress(pharmacyAddress);
        pharmacy.setNumberOfUsers(numberOfEmployees);

        pharmacyService.savePharmacy(pharmacy);

        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

}
