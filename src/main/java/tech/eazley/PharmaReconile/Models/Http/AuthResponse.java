package tech.eazley.PharmaReconile.Models.Http;

import tech.eazley.PharmaReconile.Models.Pharmacy;

public class AuthResponse {
    private String token;
    private Pharmacy pharmacy;

    public AuthResponse(String token, Pharmacy pharmacy)
    {
        this.token = token;
        this.pharmacy = pharmacy;
    }

    public String getToken() {
        return token;
    }

    public Pharmacy getPharmacy() {
        return pharmacy;
    }
}
