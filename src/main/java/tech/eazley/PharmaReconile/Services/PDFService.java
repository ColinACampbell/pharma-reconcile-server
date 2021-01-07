package tech.eazley.PharmaReconile.Services;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.eazley.PharmaReconile.Models.DrugClaim;
import tech.eazley.PharmaReconile.Models.DrugClaimResponseBody;
import tech.eazley.PharmaReconile.Utils.PDFAnnotator;
import java.io.IOException;
import java.util.ArrayList;

@Service
public class PDFService {

    @Autowired
    PDFAnnotator pdfAnnotator;

    private byte[] clientData;
    private byte[] sagicorData;

    public void setClientData(byte[] clientData) {
        this.clientData = clientData;
    }

    public void setSagicorData(byte[] sagicorData) {
        this.sagicorData = sagicorData;
    }

    public String[] readPDF(byte[] data) throws IOException
    {

        PDDocument document = PDDocument.load(data);

        //document.getNumberOfPages();
        //Instantiate PDFTextStripper class
        PDFTextStripper pdfStripper = new PDFTextStripper();

        //Retrieving text from PDF document
        String text = pdfStripper.getText(document);
        String[] lines = text.split("\\n");

        //Closing the document
        document.close();
        return lines;
    }

    public ArrayList<DrugClaim> readSagicorDocument() throws IOException {

        ArrayList<String> drugLines = new ArrayList<>();
        String[] lines = readPDF(sagicorData);

        for ( String line : lines)
        {
            if (line.contains("DRUGS"))
                drugLines.add(line);
        }

        ArrayList<DrugClaim> drugClaims = new ArrayList<>();

        for ( String line : drugLines)
        {
            String[] parts = line.split("\\s");

            DrugClaim drugClaim = new DrugClaim();
            String date = parts[0];
            String refNumber = parts[2];
            float charged = Float.parseFloat(parts[3].replaceAll(",", ""));
            float excluded = Float.parseFloat(parts[4].replaceAll(",", ""));
            float deductible = Float.parseFloat(parts[5].replaceAll(",", ""));
            float deductibleInPercentage = Float.parseFloat(parts[6].replaceAll(",", "")) / 100;
            float payable =  Float.parseFloat(parts[7].replaceAll(",", ""));

            drugClaim.setReferenceNumber(refNumber);
            drugClaim.setDate(date);
            drugClaim.setCharged(charged);
            drugClaim.setExcluded(excluded);
            drugClaim.setDeductibleMoney(deductible);
            drugClaim.setDeductiblePercentage(deductibleInPercentage);
            drugClaim.setPayable(payable);

            drugClaims.add(drugClaim);
        }
        return drugClaims;
    }

    public ArrayList<String> readClientDocument() throws IOException {

        String[] lines = readPDF(clientData);
        ArrayList<String> drugLines = new ArrayList<>();
        for (String line : lines)
        {
            if (line.contains("Jan") || line.contains("Feb") || line.contains("Mar")
                    || line.contains("Apr") || line.contains("May") || line.contains("Jun")
                    || line.contains("Jul") || line.contains("Aug") || line.contains("Sep")
                    || line.contains("Oct") || line.contains("Nov") || line.contains("Dec")
            )
            {
                drugLines.add(line);
            }

        }

        return drugLines;
    }

    public ArrayList<DrugClaimResponseBody> extractData()  {

        //Pattern pattern = Pattern.compile("(\\b[A-Z]+(?:\\s+[A-Z]+[^A-Za-z0-9])*\\b)");

        ArrayList<DrugClaim> claims = new ArrayList<>();
        ArrayList<String> purchases = new ArrayList<>();

        try {
            purchases = readClientDocument();
            claims = readSagicorDocument();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        ArrayList<DrugClaimResponseBody> responseBody = new ArrayList<>();
        for (DrugClaim drugClaim : claims)
        {
            for ( String purchase : purchases)
            {
                if (purchase.contains(drugClaim.getReferenceNumber()))
                {
                    DrugClaimResponseBody body = new DrugClaimResponseBody();
                    /**
                     Matcher matcher = pattern.matcher(purchase);
                     matcher.find();
                     System.out.println(matcher.group(0)+" "+matcher.group(1));
                     **/
                    body.setDetails(purchase.substring(12));

                    body.setReferenceNumber(drugClaim.getReferenceNumber());
                    body.setDate(drugClaim.getDate());
                    body.setCharged(drugClaim.getCharged());
                    body.setExcluded(drugClaim.getExcluded());
                    body.setDeductibleMoney(drugClaim.getExcluded());
                    body.setDeductiblePercentage(drugClaim.getDeductiblePercentage());
                    body.setPayable(drugClaim.getPayable());
                    responseBody.add(body);
                }
            }
        }

        return responseBody;
    }

    public byte[] highlightReferences(ArrayList<DrugClaimResponseBody> claimResponseBody)
    {
        String[] criteria= new String[claimResponseBody.size()];
        for (int i = 0; i < claimResponseBody.size(); i ++)
        {
            criteria[i] = claimResponseBody.get(i).getReferenceNumber();
        }

        try {
            System.out.println("Criteria length " + criteria.length);
            pdfAnnotator.setCriteria(criteria);
            return pdfAnnotator.run(clientData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}