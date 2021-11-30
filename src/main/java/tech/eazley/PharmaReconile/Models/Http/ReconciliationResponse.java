package tech.eazley.PharmaReconile.Models.Http;

import tech.eazley.PharmaReconile.Models.DrugClaim;
import tech.eazley.PharmaReconile.Models.DrugClaimResponseBody;

import java.util.ArrayList;

public class ReconciliationResponse {

    public ArrayList<DrugClaim> drugClaimResponseBodies;
    public double providerTotals = 0;

    public ReconciliationResponse(ArrayList<DrugClaim> DrugClaim, double providerTotals)
    {
        this.drugClaimResponseBodies = DrugClaim;
        this.providerTotals = providerTotals;
    }

    public void setProviderTotals(float providerTotals) {
        this.providerTotals = providerTotals;
    }

    public double getProviderTotals() {
        return providerTotals;
    }

    public void setDrugClaimResponseBodies(ArrayList<DrugClaim> drugClaimResponseBodies) {
        this.drugClaimResponseBodies = drugClaimResponseBodies;
    }

    public ArrayList<DrugClaim> getDrugClaimResponseBodies() {
        return drugClaimResponseBodies;
    }
}
