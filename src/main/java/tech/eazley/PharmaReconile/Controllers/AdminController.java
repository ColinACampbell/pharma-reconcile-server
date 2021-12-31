package tech.eazley.PharmaReconile.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.eazley.PharmaReconile.Models.Pharmacy;
import tech.eazley.PharmaReconile.Services.PharmacyService;

import java.util.ArrayList;
import java.util.List;


// Set up admin structure to enable access to this feature

@RestController()
@RequestMapping("api/admin/")
public class AdminController {

    @Autowired
    PharmacyService pharmacyService;

    @GetMapping("/pharmacy")
    public List<Pharmacy> getPharmacies()
    {
        return pharmacyService.getPharmacies();
    }

    @PutMapping("/pharmacy/{id}")
    public ResponseEntity<Pharmacy> updatePharmacy(@PathVariable int id, @RequestBody Pharmacy newPharmacyInfo)
    {
        return new ResponseEntity<>(pharmacyService.updatePharmacy(id,newPharmacyInfo), HttpStatus.OK);
    }
}
