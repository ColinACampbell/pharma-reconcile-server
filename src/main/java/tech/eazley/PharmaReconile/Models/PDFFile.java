package tech.eazley.PharmaReconile.Models;

import javax.persistence.*;

@Entity
public class PDFFile {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private PDFCache pdfCache;
    private String fileType;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    byte[] data;


    public void setPdfCache(PDFCache pdfCache) {
        this.pdfCache = pdfCache;
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


    public PDFCache getPdfCache() {
        return pdfCache;
    }

    public String getFileType() {
        return fileType;
    }
}