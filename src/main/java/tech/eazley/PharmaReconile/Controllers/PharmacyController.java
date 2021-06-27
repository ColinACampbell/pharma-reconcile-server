package tech.eazley.PharmaReconile.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tech.eazley.PharmaReconile.Models.AppUserDetails;
import tech.eazley.PharmaReconile.Models.Pharmacy;
import tech.eazley.PharmaReconile.Models.PharmacyMember;
import tech.eazley.PharmaReconile.Models.User;
import tech.eazley.PharmaReconile.Services.PharmacyMemberService;
import tech.eazley.PharmaReconile.Services.PharmacyService;
import tech.eazley.PharmaReconile.Services.UserService;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/api/pharmacy")
public class PharmacyController {

    @Autowired
    PharmacyService pharmacyService;
    @Autowired
    PharmacyMemberService pharmacyMemberService;
    @Autowired
    UserService userService;

    @GetMapping("/")
    public Pharmacy getPharmacy(Authentication authentication)
    {
        AppUserDetails appUserDetails = (AppUserDetails) authentication.getPrincipal();
        PharmacyMember pharmacyMember = pharmacyMemberService.findByUser(appUserDetails.getUser());
        return pharmacyMember.getPharmacy();
    }

    @PostMapping("/")
    public ResponseEntity<?> registerPharmacy(@RequestBody Map<String,String> requestBody,Authentication authentication)
    {
        String pharmacyName = requestBody.get("pharmacy_name");
        String phone1 = requestBody.get("phone1");
        String phone2 = requestBody.get("phone2");
        String address = requestBody.get("address");
        String parish = requestBody.get("parish");


        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        Pharmacy pharmacy = new Pharmacy(pharmacyName,phone1,phone2,address,parish);
        pharmacy.setNumberOfUsers(1);

        pharmacyService.savePharmacy(pharmacy);

        PharmacyMember pharmacyMember = new PharmacyMember();
        pharmacyMember.setUser(userDetails.getUser());
        pharmacyMember.setRole("owner");
        pharmacyMember.setPharmacy(pharmacy);

        pharmacyMemberService.addMember(pharmacyMember);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
