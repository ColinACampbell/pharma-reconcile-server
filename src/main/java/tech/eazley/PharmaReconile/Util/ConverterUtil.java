package tech.eazley.PharmaReconile.Util;

import tech.eazley.PharmaReconile.Models.Provider;
import tech.eazley.PharmaReconile.Models.Vendor;

public class ConverterUtil {
    public static Vendor stringToVendor(String vendor)
    {
        switch (vendor)
        {
            case "pharmacy-works" :
                return Vendor.PHARMACY_WORKS;
            default:
                return Vendor.PHAR_PARTNER;
        }
    }

    public static String providerToString(Provider provider)
    {
        switch (provider)
        {
            case SAGICOR:
                return "SAGICOR";
            default:
                return "NONE";
        }
    }



}
