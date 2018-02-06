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
    public boolean success;
    public String terms;
    public String privacy;
    public String timestamp;
    public String source;
    public CurrencyError error;

    @JsonProperty("quotes")
    public Map<String, BigDecimal> quotes = new HashMap<>();

    public CurrencyData() {
        this.source = "GBP";
    }

    public Map<String, BigDecimal> getQuotes() {
        return quotes;
    }
}
