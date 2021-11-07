package tech.eazley.PharmaReconile.Models;

import javax.persistence.*;
import java.util.List;

@Entity
public class PDFCache {

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
    }

    @Column(nullable = true)
    private long dateAdded; // Use locally set time instead of server time

    @OneToMany(mappedBy = "pdfCache")
    private List<PDFFile> files;


    @Column(nullable = true)
    private long fromPeriod;
    @Column(nullable = true)
    private long toPeriod;

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
    private double payable;

    @Column(nullable = true)
    private double charged;

    public PDFCache() {

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

    public void setCharged(double charged) {
        this.charged = charged;
    }

    public void setPayable(double payable) {
        this.payable = payable;
    }

    public double getCharged() {
        return charged;
    }

    public double getPayable() {
        return payable;
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
}
