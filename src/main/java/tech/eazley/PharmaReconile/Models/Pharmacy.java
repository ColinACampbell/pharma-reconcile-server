package tech.eazley.PharmaReconile.Models;

import javax.persistence.*;
import java.util.List;

@Entity
public class Pharmacy {
    @Id
    @GeneratedValue
    private int id;
    private String pharmacyName;
    private String address;
    private String phone1;
    private String phone2;
    private String parish;
    private int numberOfUsers = 0;
    @Column(nullable = true)
    private boolean isEnabled;

    @OneToMany(mappedBy = "pharmacy")
    List<PharmacyMember> members;

    @OneToOne(mappedBy = "pharmacy")
    CreditAccount creditAccount;

    @OneToMany(mappedBy = "pharmacy")
    List<Reconciliation> reconciliationCaches;

    public Pharmacy(String name, String phone1, String phone2, String address, String parish)
    {
        this.pharmacyName = name;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.address = address;
        this.parish = parish;
        this.isEnabled = true;
    }

    public  Pharmacy()
    {

    }


    public void setPharmacyName(String pharmacyName) {
        this.pharmacyName = pharmacyName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setNumberOfUsers(int numberOfUsers) {
        this.numberOfUsers = numberOfUsers;
    }

    public int getId() {
        return id;
    }

    public String getPhone1() {
        return phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public String getParish() {
        return parish;
    }

    public String getAddress() {
        return address;
    }

    public int getNumberOfUsers() {
        return numberOfUsers;
    }

    public String getPharmacyName() {
        return pharmacyName;
    }

    public boolean getIsEnabled()
    {
        return isEnabled;
    }

    public void setIsEnabled(boolean isEnabled)
    {
        this.isEnabled = isEnabled;
    }
}
