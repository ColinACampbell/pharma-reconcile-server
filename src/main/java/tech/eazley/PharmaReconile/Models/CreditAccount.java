package tech.eazley.PharmaReconile.Models;

import javax.persistence.*;
import java.util.List;

@Entity()
@Table(name="credit_accounts")
public class CreditAccount {

    @Id
    @GeneratedValue
    private  int id;
    @OneToOne
    @JoinColumn(name = "pharmacy_id")
    private Pharmacy pharmacy;

    @OneToMany(mappedBy = "creditAccount")
    List<CreditTransaction> creditTransactions;

    public CreditAccount()
    {
    }

    public void setPharmacy(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }

    public Pharmacy getPharmacy() {
        return pharmacy;
    }

    public List<CreditTransaction> getCreditTransactions() {
        return creditTransactions;
    }
}
