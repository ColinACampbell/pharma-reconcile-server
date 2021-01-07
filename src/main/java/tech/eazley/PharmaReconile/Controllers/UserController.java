package tech.eazley.PharmaReconile.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tech.eazley.PharmaReconile.Models.AppUserDetails;
import tech.eazley.PharmaReconile.Models.User;
import tech.eazley.PharmaReconile.Repositories.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("api/user/")
public class UserController {

    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/test")
    public User test(Authentication authentication)
    {
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        return userDetails.getUser();
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest request, @RequestBody Map<String,String> requestBody)
    {
        String email = requestBody.get("email");
        String password = requestBody.get("password");

        User user = userRepository.findByEmail(email);

        if (user == null)
            return new ResponseEntity<>(null,HttpStatus.FORBIDDEN);

        // Check if the encoded password matchies the user password
        if (passwordEncoder.matches(password,user.getPassword()))
        {

            String username = user.getUsername();
            // authenticate user
            SecurityContext securityContext = this.createSecurityContext(username,password);

            // Create a new session and add the security context.
            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
            return new ResponseEntity<>(null,HttpStatus.OK);
        }

        return new ResponseEntity<>(null,HttpStatus.FORBIDDEN);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody Map<String,String> requestBody, HttpServletRequest request)
    {
        String email = requestBody.get("email");
        String username = requestBody.get("username");
        String password = requestBody.get("password");

        if (userRepository.findByEmail(email) != null) {
            return new ResponseEntity<>(null,HttpStatus.CONFLICT);
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        // Create a token to auth user
        SecurityContext securityContext = this.createSecurityContext(username,password);

        // Create a new session and add the security context.
        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

        return new ResponseEntity<>(null,HttpStatus.CREATED);
    }

    @PostMapping("/check-username")
    public ResponseEntity<?> checkUsername(@RequestBody Map<String,String> requestBody)
    {
        String username = requestBody.get("username");
        User user = userRepository.findByUsername(username);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    public SecurityContext createSecurityContext(String username,String password)
    {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username,password);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authToken);
        return securityContext;
    }

}
