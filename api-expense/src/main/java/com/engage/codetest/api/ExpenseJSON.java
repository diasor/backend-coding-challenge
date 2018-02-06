package com.engage.codetest.api;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author Diana Sormani
 * Created: January 28, 2018
 * Last Updated: February 05, 2018
 * Description: The ExpenseJSON class was created to interact directly with the frontend. Its memmbers are named
 *              exactly as they were in the frontend.
 *              The results of the services are presented with this json format.
 */
public class ExpenseJSON {
    private String date;                // Date and time of the invoice date
    private BigDecimal amount;          // The amount of the expense
    private BigDecimal vat;             // The vat amount
    private String currency;            // currency code
    private String currencySymbol;      // currency symbol
    private String reason;              // Reason for the expense
    private String user;                // User

    public ExpenseJSON(){
        // Do not remove
    }

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

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getReason() {
        return reason;
    }
}