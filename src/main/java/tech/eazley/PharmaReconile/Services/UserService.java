package tech.eazley.PharmaReconile.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.eazley.PharmaReconile.Models.User;
import tech.eazley.PharmaReconile.Repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    User findByUsername(String username)
    {
        return userRepository.findByUsername(username);
    }
}
