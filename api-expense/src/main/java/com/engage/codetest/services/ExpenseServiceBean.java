package com.engage.codetest.services;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseServiceBean {
    private LocalDate expenseDate;          // Date and time of the invoice date
    private BigDecimal expenseAmount;       // The amount of the expense
    private short currencyCode;             // The currency ISO 4217 numeric code
    private String expenseReason;           // Reason for the expense

    public ExpenseServiceBean(LocalDate expenseDate, BigDecimal expenseAmount, short currencyCode, String expenseReason) {
        this.expenseDate = expenseDate;
        this.expenseAmount = expenseAmount;
        this.currencyCode = currencyCode;
        this.expenseReason = expenseReason;
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
}
