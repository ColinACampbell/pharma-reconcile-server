package tech.eazley.PharmaReconile.Models;

import javax.annotation.processing.Generated;
import javax.persistence.*;

@Entity
@Table(name = "pharmacy_members")
public class PharmacyMember {


    @Id
    @GeneratedValue
    private int id;

    @OneToOne(mappedBy = "pharmacyMember")
    private User user;
    @ManyToOne()
    private Pharmacy pharmacy;

    private String role;

    public PharmacyMember()
    {

    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPharmacy(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }

    public User getUser() {
        return user;
    }

    public Pharmacy getPharmacy() {
        return pharmacy;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

