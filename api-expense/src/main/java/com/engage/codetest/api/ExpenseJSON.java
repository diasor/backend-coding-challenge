package com.engage.codetest.api;

import java.math.BigDecimal;
import java.util.*;

/**
 * This class was created to interact directly with the frontend. Its memmbers are named
 * exactly as they were in the frontend.
 */
public class ExpenseJSON {
    private String date;                // Date and time of the invoice date
    private BigDecimal amount;          // The amount of the expense
    private BigDecimal vat;             // The vat amount
    private String currency;            // currency code
    private String currencySymbol;      // currency symbol
    private String reason;              // Reason for the expense
    private String user;                // User

    public ExpenseJSON(){}

    public ExpenseJSON(String date, BigDecimal amount, BigDecimal vat, String currency, String currencySymbol, String reason, String user) {
        this.date = date;
        this.amount = amount;
        this.vat = vat;
        this.currency = currency;
        this.currencySymbol = currencySymbol;
        this.reason = reason;
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public BigDecimal getVat() {
        return vat;
    }

    public void setVat(BigDecimal vat) {
        this.vat = vat;
    }
}