package tech.eazley.PharmaReconile.Models;

public class DrugClaim {

    public DrugClaim()
    {

    }

    private String date;
    private String referenceNumber;
    private float charged;
    private float excluded;
    private float deductibleMoney;
    private float deductiblePercentage;
    private float payable;

    public void setDate(String date) {
        this.date = date;
    }

    public void setCharged(float charged) {
        this.charged = charged;
    }

    public void setDeductibleMoney(float deductibleMoney) {
        this.deductibleMoney = deductibleMoney;
    }

    public void setDeductiblePercentage(float deductiblePercentage) {
        this.deductiblePercentage = deductiblePercentage;
    }

    public void setExcluded(float excluded) {
        this.excluded = excluded;
    }

    public void setPayable(float payable) {
        this.payable = payable;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getDate() {
        return date;
    }

    public float getCharged() {
        return charged;
    }

    public float getExcluded() {
        return excluded;
    }

    public float getDeductibleMoney() {
        return deductibleMoney;
    }

    public float getDeductiblePercentage() {
        return deductiblePercentage;
    }

    public float getPayable() {
        return payable;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }
}