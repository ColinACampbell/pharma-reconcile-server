package tech.eazley.PharmaReconile.Models;

import javax.persistence.*;

@Entity
@Table( name = "users")
public class User {

    public User()
    {

    }

    @Id
    @GeneratedValue
    private int id;
    private String username;
    private String email;
    private String password;
    private String role;

    @ManyToOne
    private Pharmacy pharmacy;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setPharmacy(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public Pharmacy getPharmacy() {
        return pharmacy;
    }
}
