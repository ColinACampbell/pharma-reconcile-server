package tech.eazley.PharmaReconile.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tech.eazley.PharmaReconile.Models.*;
import tech.eazley.PharmaReconile.Models.Http.AuthResponse;
import tech.eazley.PharmaReconile.Services.CreditService;
import tech.eazley.PharmaReconile.Services.PharmacyMemberService;
import tech.eazley.PharmaReconile.Services.PharmacyService;
import tech.eazley.PharmaReconile.Services.UserService;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
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

    @Autowired
    CreditService creditService;

    @GetMapping("/")
    public Pharmacy getPharmacy(Authentication authentication)
    {
        AppUserDetails appUserDetails = (AppUserDetails) authentication.getPrincipal();
        PharmacyMember pharmacyMember = pharmacyMemberService.findByUser(appUserDetails.getUser());
        return pharmacyMember.getPharmacy();
    }

    @PostMapping("/")
    public AuthResponse registerPharmacy(@RequestBody Map<String,String> requestBody,Authentication authentication)
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

        // Add credit
        //CreditAccount creditAccount = new CreditAccount();
        //creditAccount.setPharmacy(pharmacy);

        //CreditTransaction creditTransaction = new CreditTransaction(1000,"Free Credit",new Timestamp(50000));

        //creditService.saveCreditAccount(creditAccount);
        //creditService.saveTransaction(creditTransaction,creditAccount);

        // Token is not consumed so it's not needed
        return new AuthResponse("",pharmacy);
    }
}
