package tech.eazley.PharmaReconile.Util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import tech.eazley.PharmaReconile.Models.DrugClaim;

import java.util.ArrayList;
import java.util.List;

public class ObjectMapperUtil {
    public static String drugClaimsToJson(ArrayList<DrugClaim> drugClaims)
    {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(drugClaims);
        } catch (JsonProcessingException e)
        {
            e.printStackTrace();
        }
        return "[]";
    }
}
