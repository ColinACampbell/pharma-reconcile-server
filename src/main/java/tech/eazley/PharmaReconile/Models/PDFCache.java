package tech.eazley.PharmaReconile.Models;

import javax.persistence.*;
import java.util.List;

@Entity
public class PDFCache {
    @Id
    @GeneratedValue
    private int id;
    private long dataAdded;

    @OneToMany(mappedBy = "pdfCache")
    private List<PDFFile> files;

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

    public long getDataAdded() {
        return dataAdded;
    }

    public List<PDFFile> getFiles() {
        return files;
    }
}
