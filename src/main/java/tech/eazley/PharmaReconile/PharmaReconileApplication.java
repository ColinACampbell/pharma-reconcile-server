package tech.eazley.PharmaReconile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech.eazley.PharmaReconile.Models.Pharmacy;
import tech.eazley.PharmaReconile.Models.PharmacyMember;
import tech.eazley.PharmaReconile.Models.User;
import tech.eazley.PharmaReconile.Services.PharmacyMemberService;
import tech.eazley.PharmaReconile.Services.PharmacyService;
import tech.eazley.PharmaReconile.Services.UserService;

@SpringBootApplication
public class PharmaReconileApplication {

	@Autowired
	UserService userService;
	@Autowired
	PharmacyMemberService pharmacyMemberService;
	@Autowired
	PharmacyService pharmacyService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(PharmaReconileApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void adminSetUp() {
		// Check if an admin account already exists
		User user = userService.findByEmail("colina.campbell.jr@gmail.com");

		if (user == null)
		{
			User newAdminUser = new User();
			newAdminUser.setEmail("colina.campbell.jr@gmail.com");
			newAdminUser.setPassword(passwordEncoder.encode("password12#"));
			newAdminUser.setRole("owner");
			newAdminUser.setUsername("ColinTheAdmin");


			PharmacyMember pharmacyMember = new PharmacyMember();
			pharmacyMember.setUser(newAdminUser);
			pharmacyMember.setRole("owner");

			//newAdminUser.setPharmacyMember(pharmacyMember);

			Pharmacy pharmacy = new Pharmacy();
			pharmacy.setPharmacyName("Admin Pharmacy");
			pharmacy.setParish("St. Catherine");
			pharmacy.setAddress("Test");
			pharmacy.setPhone1("123");
			pharmacy.setPhone2("123");
			pharmacy.setNumberOfUsers(1);
			pharmacy.setPaymentPeriodDays(100000000);
			pharmacy.setNextPaymentDate(0L);
			pharmacy.setLastPaymentDate(0L);
			pharmacy.setIsEnabled(true);
			pharmacy.setAdminPharmacy(true);

			pharmacyMember.setPharmacy(pharmacy);

			userService.save(newAdminUser);

			pharmacyService.savePharmacy(pharmacy);
			pharmacyMemberService.addMember(pharmacyMember);

			newAdminUser.setPharmacyMember(pharmacyMember);
			userService.save(newAdminUser);

			// create user and check if pharmacy exists
			// if not create admin pharmacy
		} else {
			PharmacyMember pharmacyMember = pharmacyMemberService.findByUser(user);
			Pharmacy pharmacy = pharmacyMember.getPharmacy();
			pharmacy.setIsEnabled(true);
			pharmacy.setAdminPharmacy(true);
			pharmacyService.savePharmacy(pharmacy);

			user.setPassword(passwordEncoder.encode("password12#"));
			userService.save(user);
			System.out.print("Updated User Password");
		}
	}

}
