package tech.eazley.PharmaReconile.Controllers.PartnerProgram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.eazley.PharmaReconile.Models.Http.PartnerProgram.Partner;

@Repository
interface PharmaPartnerRepository extends CrudRepository<Partner,Integer> {
    Partner findByEmail(String email);
}

@Service
class PharmaPartnerService {
    @Autowired
    PharmaPartnerRepository pharmaPartnerRepository;

    public Partner findByEmail(String email)
    {
        return pharmaPartnerRepository.findByEmail(email);
    }
    public void save(Partner partner)
    {
        pharmaPartnerRepository.save(partner);
    }
}

@RestController
@RequestMapping("/api/partner")
public class PartnerController {

    @Autowired
    PharmaPartnerService pharmaPartnerService;

    @PostMapping("/")
    ResponseEntity<?> savePartner(@RequestBody Partner partner)
    {
        if (pharmaPartnerService.findByEmail(partner.getEmail()) != null)
            return new ResponseEntity<>(null,HttpStatus.CONFLICT);

        pharmaPartnerService.save(partner);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }
}
