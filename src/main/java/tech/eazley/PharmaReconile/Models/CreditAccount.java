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
    private int credits = 0;

    @OneToMany(mappedBy = "creditAccount")
    List<CreditTransaction> creditTransactions;

    CreditAccount()
    {

    }

    void setCredit(int credit)
    {
        this.credits = credit;
    }

    void addCredit(int credit)
    {
        this.credits += credit;
    }

    public void setPharmacy(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }

    public int getCredit() {
        return credits;
    }

    public Pharmacy getPharmacy() {
        return pharmacy;
    }

    public List<CreditTransaction> getCreditTransactions() {
        return creditTransactions;
    }
}
