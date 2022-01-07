package tech.eazley.PharmaReconile.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tech.eazley.PharmaReconile.Models.AppUserDetails;
import tech.eazley.PharmaReconile.Models.Pharmacy;
import tech.eazley.PharmaReconile.Models.PharmacyMember;
import tech.eazley.PharmaReconile.Services.PharmacyMemberService;
import tech.eazley.PharmaReconile.Services.PharmacyService;
import java.util.List;

// Set up admin structure to enable access to this feature
@RestController()
@RequestMapping("api/admin/")
public class AdminController {

    @Autowired
    PharmacyService pharmacyService;
    @Autowired
    PharmacyMemberService pharmacyMemberService;

    @GetMapping("/pharmacy")
    public List<Pharmacy> getPharmacies()
    {
        return pharmacyService.getPharmacies();
    }

    @PutMapping("/pharmacy/{id}")
    public ResponseEntity<Pharmacy> updatePharmacy(Authentication authentication,@PathVariable int id, @RequestBody Pharmacy newPharmacyInfo)
    {
        boolean isAdmin = getPharmacyMember(authentication).getPharmacy().isAdminPharmacy() != null && getPharmacyMember(authentication).getPharmacy().isAdminPharmacy();
        if (isAdmin)
            return new ResponseEntity<>(pharmacyService.updatePharmacy(id,newPharmacyInfo), HttpStatus.OK);
        else
            return new ResponseEntity<>(new Pharmacy(),HttpStatus.FORBIDDEN);
    }

    private PharmacyMember getPharmacyMember(Authentication authentication)
    {
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        return pharmacyMemberService.findByUser(userDetails.getUser());
    }
}
