package tech.eazley.PharmaReconile.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.eazley.PharmaReconile.Models.PharmacyMember;
import tech.eazley.PharmaReconile.Models.User;
import tech.eazley.PharmaReconile.Repositories.PharmacyMemberRepository;

@Service
public class PharmacyMemberService {
    @Autowired
    PharmacyMemberRepository pharmacyMemberRepository;
    public PharmacyMember findByUser(User user)
    {
        return pharmacyMemberRepository.findByUser(user);
    }

    public  void addMember(PharmacyMember member)
    {
        pharmacyMemberRepository.save(member);
    }
}
