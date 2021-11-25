package tech.eazley.PharmaReconile.Models.Http;

import tech.eazley.PharmaReconile.Models.DrugClaimResponseBody;

import java.util.ArrayList;

public class ReconciliationResponse {

    public ArrayList<DrugClaimResponseBody> drugClaimResponseBodies;
    public double providerTotals = 0;

    public ReconciliationResponse(ArrayList<DrugClaimResponseBody> drugClaimResponseBodies, double providerTotals)
    {
        this.drugClaimResponseBodies = drugClaimResponseBodies;
        this.providerTotals = providerTotals;
    }

    public void setProviderTotals(float providerTotals) {
        this.providerTotals = providerTotals;
    }

    public double getProviderTotals() {
        return providerTotals;
    }

    public void setDrugClaimResponseBodies(ArrayList<DrugClaimResponseBody> drugClaimResponseBodies) {
        this.drugClaimResponseBodies = drugClaimResponseBodies;
    }

    public ArrayList<DrugClaimResponseBody> getDrugClaimResponseBodies() {
        return drugClaimResponseBodies;
    }
}
