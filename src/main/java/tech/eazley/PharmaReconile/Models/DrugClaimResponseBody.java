package tech.eazley.PharmaReconile.Models;

public class DrugClaimResponseBody extends DrugClaim {
    private String details;
    public DrugClaimResponseBody()
    {

    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}

