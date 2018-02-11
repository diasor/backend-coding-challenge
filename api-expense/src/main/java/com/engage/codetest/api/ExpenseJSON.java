package com.engage.codetest.api;

import java.math.BigDecimal;

/**
 * @author Diana Sormani
 * Created: January 28, 2018
 * Last Updated: February 05, 2018
 * Description: The ExpenseJSON class was created to interact directly with the frontend. Its memmbers are named
 * exactly as they were in the frontend.
 * The results of the services are presented with this json format.
 */
public class ExpenseJSON {
    private int id;                 // This id is only created with the purpose of returning the id assigned when creating a new expense
    private String date;            // Date and time of the invoice date
    private String amount;          // The amount of the expense
    private BigDecimal vat;         // The vat amount
    private String currency;        // currency code
    private String currencySymbol;  // currency symbol
    private String reason;          // Reason for the expense
    private String user;            // User

    public ExpenseJSON() {
        // Do not remove
    }

    public ExpenseJSON(int id, String date, String amount, BigDecimal vat, String currency, String currencySymbol, String reason, String user) {
        this.id = id;
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

    public String getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getReason() {
        return reason;
    }

    public BigDecimal getVat() {
        // DO NOT remove :: used from frontend
        return vat;
    }

    public String getCurrencySymbol() {
        // DO NOT remove :: used from frontend
        return currencySymbol;
    }

    public String getUser() {
        return user;
    }

    public int getId() {
        return id;
    }
}