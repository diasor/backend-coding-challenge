package com.engage.codetest.api;

import java.math.BigDecimal;
import java.util.*;

public class Expense {
    private int id;                         // Unique id for the expenses
    private Date expenseDate;               // Date and time of the invoice date
    private BigDecimal expenseAmount;       // The amount of the expense
    private BigDecimal expenseAmnVAT;       // Value added tax associated to the expense (20% of expense amount)
    private Currency amountCurrencyISO;     // Currency in ISO 4217 NUMERIC CODE
    private String expenseReason;           // Reason for the expense

    public Expense(int eId, Date eDate, BigDecimal eAmount, Currency eCurrency, String eReason){
        this.id = eId;
        this.expenseDate = eDate;
        this.expenseAmount = eAmount;
        this.expenseAmnVAT = eAmount.multiply(BigDecimal.valueOf(0.2));
        this.amountCurrencyISO = eCurrency;
        this.expenseReason = eReason;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(Date expenseDate) {
        this.expenseDate = expenseDate;
    }

    public BigDecimal getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(BigDecimal expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public BigDecimal getExpenseAmnVAT() {
        return expenseAmnVAT;
    }

    public void setExpenseAmnVAT(BigDecimal expenseAmnVAT) {
        this.expenseAmnVAT = expenseAmnVAT;
    }

    public Currency getAmountCurrencyISO() {
        return amountCurrencyISO;
    }

    public void setAmountCurrencyISO(Currency amountCurrencyISO) {
        this.amountCurrencyISO = amountCurrencyISO;
    }

    public String getExpenseReason() {
        return expenseReason;
    }

    public void setExpenseReason(String expenseReason) {
        this.expenseReason = expenseReason;
    }
}