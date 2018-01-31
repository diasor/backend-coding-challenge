package com.engage.codetest.dao;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseDaoBean {
    private int id;                         // Unique id for the expenses
    private LocalDate expenseDate;          // Date and time of the invoice date
    private BigDecimal expenseAmount;       // The amount of the expense
    private BigDecimal expenseAmountVAT;    // The VAT amount of the expense
    private short currencyCode;             // The currency ISO 4217 numeric code
    private String expenseReason;           // Reason for the expense

    public ExpenseDaoBean(int id, LocalDate expenseDate, BigDecimal expenseAmount, BigDecimal expenseAmountVAT, short currencyCode, String expenseReason) {
        this.id = id;
        this.expenseDate = expenseDate;
        this.expenseAmount = expenseAmount;
        this.expenseAmountVAT = expenseAmountVAT;
        this.currencyCode = currencyCode;
        this.expenseReason = expenseReason;
    }

    public ExpenseDaoBean(LocalDate expenseDate, BigDecimal expenseAmount, BigDecimal expenseAmountVAT, short currencyCode, String expenseReason) {
        this.expenseDate = expenseDate;
        this.expenseAmount = expenseAmount;
        this.expenseAmountVAT = expenseAmountVAT;
        this.currencyCode = currencyCode;
        this.expenseReason = expenseReason;
    }

    public ExpenseDaoBean(){}

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
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

    public short getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(short currencyCode) {
        this.currencyCode = currencyCode;
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
}