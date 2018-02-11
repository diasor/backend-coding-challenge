package com.engage.codetest.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Diana Sormani
 * Created: February 03, 2018
 * Last Updated: February 05, 2018
 * Description: The CurrencyData class was created to store the information provided by the "apilayer" rest endpoint for
 *              obtaining the convertion rates betweeen different currencies. Its memmbers are named
 *              exactly as they are defined in the endpoint: apilayer.
 */
public class CurrencyData {
    private boolean success;
    private String terms;
    private String privacy;
    private String timestamp;
    private String source;
    private CurrencyError error;

    @JsonProperty("quotes")
    public Map<String, BigDecimal> quotes = new HashMap<>();

    public CurrencyData() {
        // Do not remove
        this.source = "GBP";
    }

    public CurrencyData(boolean success){
        // This constructor was created for testing purposes
        this.success = success;
        this.terms = "https://currencylayer.com/terms";
        this.privacy = "https://currencylayer.com/privacy";
        this.timestamp = "1518106152";
        this.source = "GBP";
    }

    public Map<String, BigDecimal> getQuotes() {
        return quotes;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getTerms() {
        return terms;
    }

    public String getPrivacy() {
        return privacy;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getSource() {
        return source;
    }

    public CurrencyError getError() {
        return error;
    }

    public void setError(CurrencyError error) {
        this.error = error;
    }
}