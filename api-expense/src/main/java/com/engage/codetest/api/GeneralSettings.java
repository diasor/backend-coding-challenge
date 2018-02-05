package com.engage.codetest.api;

import java.math.BigDecimal;

public final class GeneralSettings {
    private static String apiURL = "http://apilayer.net/api/live?";
    private static String apiKey = "a7a73a0a5c4bb342db32b7ec1728a7d8";
    private static BigDecimal VAT = new BigDecimal(20);

    private GeneralSettings(){}

    public static String getApiURL() {
        return apiURL;
    }

    public static  String getApiKey() {
        return apiKey;
    }

    public static BigDecimal getVAT() {return VAT; }

}
