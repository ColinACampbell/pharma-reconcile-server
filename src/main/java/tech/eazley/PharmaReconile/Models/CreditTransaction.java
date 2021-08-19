package tech.eazley.PharmaReconile.Models;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="credit_transactions")
public class CreditTransaction {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne(targetEntity = CreditAccount.class)
    @JoinColumn(name = "credit_account_id")
    private CreditAccount creditAccount;

    private String type;
    private Timestamp timestamp;
    private int transactionAmount;

    public CreditTransaction()
    {

    }

    public CreditTransaction(int transactionAmount,String type, Timestamp timestamp)
    {
        this.transactionAmount = transactionAmount;
        this.type = type;
        this.timestamp = timestamp;
    }


    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public int getTransactionAmount() {
        return transactionAmount;
    }

    public void setCreditAccount(CreditAccount creditAccount) {
        this.creditAccount = creditAccount;
    }

    public CreditAccount getCreditAccount() {
        return creditAccount;
    }
}
