package tech.eazley.PharmaReconile.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tech.eazley.PharmaReconile.Models.AppUserDetails;
import tech.eazley.PharmaReconile.Models.Pharmacy;
import tech.eazley.PharmaReconile.Services.PharmacyService;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/api/pharmacy")
public class PharmacyController {

    @Autowired
    PharmacyService pharmacyService;

    @PostMapping("/register-pharmacy")
    public ResponseEntity<?> registerPharmacy(@RequestBody Map<String,String> postBody, HttpSession session)
    {

        System.out.print(session);

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


    @GetMapping("/")
    public Pharmacy getPharmacy(Authentication authentication)
    {
        AppUserDetails appUserDetails = (AppUserDetails) authentication.getPrincipal();
        Pharmacy pharmacy = pharmacyService.findByUser(appUserDetails.user);
        System.out.println();
        return pharmacy;
    }
}
