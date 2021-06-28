package tech.eazley.PharmaReconile.Models;

import javax.persistence.*;

@Entity
@Table(name = "pharmacy_members")
public class PharmacyMember {
    @Id
    @GeneratedValue
    private int id;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(cascade = CascadeType.REMOVE)
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

    public String getRole() {
        return role;
    }
}

