package tech.eazley.PharmaReconile.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.eazley.PharmaReconile.Models.Pharmacy;

import java.util.ArrayList;
import java.util.List;

@RestController()
@RequestMapping("api/admin/")
public class AdminController {
    @GetMapping("/pharmacy")
    public List<Pharmacy> getPharmacies()
    {
        return new ArrayList<>();
    }
}
