package tech.eazley.PharmaReconile.Models;


import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class PDFCache {
    @Id
    @GeneratedValue
    private int id;
    private long dataAdded;

    @OneToMany(mappedBy = "pdfCache")
    private List<PDFFile> files;

    public int getId() {
        return id;
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
