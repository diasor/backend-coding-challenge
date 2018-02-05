package com.engage.codetest.services;

import com.engage.codetest.services.CurrencyError;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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
