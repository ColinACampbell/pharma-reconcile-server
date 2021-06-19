package tech.eazley.PharmaReconile.Models;

import javax.persistence.*;
import java.util.List;

@Entity
public class Pharmacy {
    @Id
    @GeneratedValue
    private int id;
    @OneToOne
    private User user;
    private String pharmacyName;
    private String address;
    private String phone1;
    private String phone2;
    private String parish;
    private int numberOfUsers = 0;

    public Pharmacy(String name, String phone1, String phone2, String address, String parish)
    {
        this.pharmacyName = name;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.address = address;
        this.parish = parish;
    }

    public  Pharmacy()
    {

    }

    // add billing info
    @OneToMany( mappedBy = "pharmacy")
    private List<User> users;

    public void setPharmacyName(String pharmacyName) {
        this.pharmacyName = pharmacyName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setNumberOfUsers(int numberOfUsers) {
        this.numberOfUsers = numberOfUsers;
    }
}
