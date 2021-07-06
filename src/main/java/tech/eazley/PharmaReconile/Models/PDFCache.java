package tech.eazley.PharmaReconile.Models;

import javax.persistence.*;
import java.util.List;

@Entity
public class PDFCache {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;
    private long dataAdded;

    @OneToMany(mappedBy = "pdfCache")
    private List<PDFFile> files;


    @Column(nullable = true)
    private long fromPeriod;
    @Column(nullable = true)
    private long toPeriod;

    @ManyToOne()
    @JoinColumn(name = "pharmacy_id")
    private Pharmacy pharmacy;

    public int getId() {
        return id;
    }


    public void setPharmacy(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }


    public Pharmacy getPharmacy() {
        return pharmacy;
    }

    public void setDataAdded(long dataAdded) {
        this.dataAdded = dataAdded;
    }

    public void setFromPeriod(long fromPeriod) {
        this.fromPeriod = fromPeriod;
    }

    public void setToPeriod(long toPeriod) {
        this.toPeriod = toPeriod;
    }

    public long getDataAdded() {
        return dataAdded;
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
