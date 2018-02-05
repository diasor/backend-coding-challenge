package com.engage.codetest.dao;

import com.engage.codetest.api.GeneralSettings;
import com.engage.codetest.services.CurrencyService;

import javax.ws.rs.client.Client;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Currency;

public class ExpenseDaoBean {
    private int id;                         // Unique id for the expenses
    private LocalDate expenseDate;          // Date and time of the invoice date
    private BigDecimal expenseAmount;       // The amount of the expense
    private BigDecimal expenseAmountVAT;    // The VAT amount of the expense
    private String currencyCode;            // The currency ISO 4217 string code
    private String expenseReason;           // Reason for the expense
    private String expenseUser;             // User who generated the expense

    private Currency currency;

    public ExpenseDaoBean(int id, LocalDate expenseDate, BigDecimal expenseAmount, BigDecimal expenseAmountVAT, String currencyCode, String expenseReason, String expenseUser) {
        this.id = id;
        this.expenseDate = expenseDate;
        this.expenseAmount = expenseAmount;
        this.expenseAmountVAT = expenseAmountVAT;
        this.currencyCode = currencyCode;
        this.expenseReason = expenseReason;
        this.expenseUser = expenseUser;

        if ((currencyCode == null) || currencyCode.isEmpty()) {
            this.currencyCode = "GBP";
        }
        else {
            this.currencyCode = currencyCode;
        }
        this.currency = Currency.getInstance(currencyCode);
    }

    public ExpenseDaoBean(LocalDate expenseDate, BigDecimal expenseAmount, String currencyCode, String expenseReason, String user) {
        this.expenseDate = expenseDate;
        this.expenseAmount = expenseAmount;
        this.expenseReason = expenseReason;
        this.expenseUser = user;

        BigDecimal vat = GeneralSettings.getVAT();
        this.expenseAmountVAT = expenseAmount.multiply(vat);

        if ((currencyCode == null) || currencyCode.isEmpty()) {
            this.currencyCode = "GBP";
        }
        else {
            this.currencyCode = currencyCode;
        }
        this.currency = Currency.getInstance(this.currencyCode);
    }

    public void currencyCalculations(Client currencyClient){
        System.out.println("DAO BEN currencycalculation");
        if (!this.currency.getCurrencyCode().equals("GBP")) {
            // if the amount is not specified in pounds, then it must be converted
            this.expenseAmount = CurrencyService.convert(this.expenseAmount, "GBP");
            this.setCurrencyCode("GBP");
        }
    }

    public ExpenseDaoBean(){}

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public String getExpenseDateString(DateTimeFormatter formatter) {
        return expenseDate.format(formatter);
    }

    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }

    public BigDecimal getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(BigDecimal expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public BigDecimal getExpenseAmountVAT() {
        return expenseAmountVAT;
    }

    public void setExpenseAmountVAT(BigDecimal expenseAmountVAT) {
        this.expenseAmountVAT = expenseAmountVAT;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
        this.currency = Currency.getInstance(currencyCode);
    }

    public String getExpenseReason() {
        return expenseReason;
    }

    public void setExpenseReason(String expenseReason) {
        this.expenseReason = expenseReason;
    }

    public int getId() {
        return id;
    }

    public void setExpenseUser(String expenseUser) {
        this.expenseUser = expenseUser;
    }

    public String getExpenseUser() {
        return expenseUser;
    }

    public Currency getCurrency() {
        return currency;
    }
}