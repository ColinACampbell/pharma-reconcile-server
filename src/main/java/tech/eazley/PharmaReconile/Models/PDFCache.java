package tech.eazley.PharmaReconile.Models;

import javax.persistence.*;
import java.util.List;

@Entity
public class PDFCache {

    public interface PDFCacheProjection
    {
        Integer getId();
        Long getFromPeriod();
        Long getToPeriod();
        Long getDateAdded();
    }

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

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

    public PDFCache()
    {

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
}
