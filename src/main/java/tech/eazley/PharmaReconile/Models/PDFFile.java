package tech.eazley.PharmaReconile.Models;

import javax.persistence.*;

@Entity
public class PDFFile {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private Reconciliation reconciliation;
    private String fileType;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    byte[] data;


    public PDFFile()
    {

    }

    public void setReconciliationCache(Reconciliation reconciliation) {
        this.reconciliation = reconciliation;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public int getId() {
        return id;
    }

    public byte[] getData() {
        return data;
    }


    public Reconciliation getReconciliationCache() {
        return reconciliation;
    }

    public String getFileType() {
        return fileType;
    }
}