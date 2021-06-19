package tech.eazley.PharmaReconile.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tech.eazley.PharmaReconile.Models.AppUserDetails;
import tech.eazley.PharmaReconile.Models.Http.AuthResponse;
import tech.eazley.PharmaReconile.Models.Http.UserRequestBody;
import tech.eazley.PharmaReconile.Models.Pharmacy;
import tech.eazley.PharmaReconile.Models.User;
import tech.eazley.PharmaReconile.Repositories.UserRepository;
import tech.eazley.PharmaReconile.Services.AppUserDetailsService;
import tech.eazley.PharmaReconile.Services.PharmacyService;
import tech.eazley.PharmaReconile.Services.UserService;
import tech.eazley.PharmaReconile.Util.JWTUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("api/user/")
public class UserController {

    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private UserService userService;
    @Autowired
    private AppUserDetailsService appUserDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PharmacyService pharmacyService;
    @Autowired
    private JWTUtil jwtUtil;

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

        User user = userService.findByEmail(email);

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
            session.setAttribute("user",user);

            return new ResponseEntity<>(null,HttpStatus.OK);
        }

        return new ResponseEntity<>(null,HttpStatus.FORBIDDEN);
    }


    @PostMapping("/auth")
    public ResponseEntity<AuthResponse> loginJWT(@RequestBody UserRequestBody userRequestBody)
    {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userRequestBody.getEmail(),userRequestBody.getPassword());
        try {
            Authentication authentication = authManager.authenticate(authenticationToken);
        } catch (BadCredentialsException ex)
        {
            ex.printStackTrace();
        }

        AppUserDetails appUserDetails = (AppUserDetails) appUserDetailsService.loadUserByUsername(userRequestBody.getEmail());
        String token = jwtUtil.generateToken(appUserDetails);
        return new ResponseEntity<AuthResponse>(new AuthResponse(token), HttpStatus.OK);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody Map<String,String> requestBody, HttpServletRequest request)
    {
        String email = requestBody.get("email");
        String username = requestBody.get("username");
        String password = requestBody.get("password");

        if (userService.findByEmail(email) != null) {
            return new ResponseEntity<>(null,HttpStatus.CONFLICT);
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        user.setRole("owner");
        userService.save(user);

        // Create a token to auth user
        SecurityContext securityContext = this.createSecurityContext(username,password);

        // Create a new session and add the security context.
        HttpSession session = request.getSession(true);

        session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
        session.setAttribute("user",user); // set the user object to be used elsewhere

        return new ResponseEntity<>(null,HttpStatus.CREATED);
    }

    @PostMapping("/check-username-and-email")
    public ResponseEntity<?> checkUsername(@RequestBody Map<String,String> requestBody, HttpServletRequest request)
    {
        String username = requestBody.get("username");
        String email = requestBody.get("email");

        User user1 = userService.findByUsername(username);
        User user2 = userService.findByEmail(email);

        if (user1 == null && user2 == null)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PostMapping("/register-pharmacy")
    public ResponseEntity<?> registerPharmacy(@RequestBody Map<String,String> requestBody)
    {
        String pharmacyName = requestBody.get("pharmacy_name");
        String phone1 = requestBody.get("phone1");
        String phone2 = requestBody.get("phone2");
        String address = requestBody.get("address");
        String parish = requestBody.get("parish");

        System.out.println(requestBody);
        Pharmacy pharmacy = new Pharmacy(pharmacyName,phone1,phone2,address,parish);
        pharmacy.setNumberOfUsers(1);
        pharmacyService.savePharmacy(pharmacy);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public SecurityContext createSecurityContext(String username,String password)
    {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username,password);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authToken);
        return securityContext;
    }

}
