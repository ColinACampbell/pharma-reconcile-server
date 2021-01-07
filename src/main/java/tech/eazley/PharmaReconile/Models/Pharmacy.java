package tech.eazley.PharmaReconile.Models;

import javax.persistence.*;
import java.util.List;

@Entity
public class Pharmacy {
    @Id
    @GeneratedValue
    private int id;
    private String pharmacyName;
    // add billing info
    @OneToMany( mappedBy = "pharmacy")
    private List<User> users;
}
