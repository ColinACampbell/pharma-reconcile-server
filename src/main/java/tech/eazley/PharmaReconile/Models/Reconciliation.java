package tech.eazley.PharmaReconile.Models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "reconciliations")
public class Reconciliation {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    public interface PDFCacheProjection
    {
        Integer getId();
        Long getFromPeriod();
        Long getToPeriod();
        Long getDateAdded();
        String getProvider();
        float getCharged();
        float getPayable();
        float getSagicorTotals();
    }

    @Column(nullable = true)
    private long dateAdded; // Use locally set time instead of server time

    @OneToMany(mappedBy = "reconciliation")
    private List<PDFFile> files;

    @Column(nullable = true)
    private long fromPeriod;
    @Column(nullable = true)
    private long toPeriod;

    @Lob
    @Column(nullable = true)
    private String reconciliationDetails;

    @ManyToOne()
    @JoinColumn(name = "pharmacy_id")
    private Pharmacy pharmacy;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    Vendor vendor;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    Provider provider;

    @Column(nullable = true)
    private float payable;

    @Column(nullable = true)
    private float charged;

    @Column(nullable = true)
    private float sagicorTotals; // transaction and gct included

    public Reconciliation() {

    }

    public int getId() {
        return id;
    }

    public void setPharmacy(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }


    public Pharmacy getPharmacy() {
        return pharmacy;
    }

    public void setDateAdded(long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void setFromPeriod(long fromPeriod) {
        this.fromPeriod = fromPeriod;
    }

    public void setToPeriod(long toPeriod) {
        this.toPeriod = toPeriod;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public void setCharged(float charged) {
        this.charged = charged;
    }

    public void setPayable(float payable) {
        this.payable = payable;
    }

    public void setSagicorTotals(float sagicorTotals) {
        this.sagicorTotals = sagicorTotals;
    }

    public float getCharged() {
        return charged;
    }

    public float getPayable() {
        return payable;
    }

    public float getSagicorTotals() {
        return sagicorTotals;
    }

    public long getDateAdded() {
        return dateAdded;
    }

    public List<PDFFile> getFiles() {
        return files;
    }

    public long getFromPeriod() {
        return fromPeriod;
    }

    public long getToPeriod() {
        return toPeriod;
    }

    public Provider getProvider() {
        return provider;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setReconciliationDetails(String reconciliationDetails) {
        this.reconciliationDetails = reconciliationDetails;
    }

    public String getReconciliationDetails() {
        return reconciliationDetails;
    }
}
