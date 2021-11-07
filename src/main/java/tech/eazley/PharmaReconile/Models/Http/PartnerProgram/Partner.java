package tech.eazley.PharmaReconile.Models.Http.PartnerProgram;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name="pharmacy_partners")
public class Partner {
    @Id
    @GeneratedValue
    int id;
    String email;
    String phone;
    String phone2;
    String pharmacyName;
    String pharmacyBranch;

    public Partner()
    {}

    public Partner(String email,String phone,String phone2,String pharmacyName,String pharmacyBranch) {
        this.email = email;
        this.phone = phone;
        this.phone2 = phone2;
        this.pharmacyName = pharmacyName;
        this.pharmacyBranch = pharmacyBranch;
    }

    public String getEmail() {
        return email;
    }

    public String getPharmacyBranch() {
        return pharmacyBranch;
    }

    public String getPharmacyName() {
        return pharmacyName;
    }

    public String getPhone() {
        return phone;
    }

    public String getPhone2() {
        return phone2;
    }
}


