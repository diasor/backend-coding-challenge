package com.engage.codetest.dao;

import com.engage.codetest.api.GeneralSettings;
import com.engage.codetest.services.CurrencyService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Currency;

/**
 * @author Diana Sormani
 * Created: January 28, 2018
 * Last Updated: February 05, 2018
 * Description: The ExpenseDaoBean class contains all the information related to the entity
 *              "Expense": both the obtained by the front end application as well
 *              as the information calculated locally.
 */
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
        //
        if ((currencyCode == null) || currencyCode.isEmpty()) {
            this.currencyCode = "GBP";
        }
        else {
            this.currencyCode = currencyCode;
        }
        this.currency = Currency.getInstance(this.currencyCode);

        // Vat calculation
        this.expenseAmountVAT = calcExpenseAmountVAT();
    }

    /**
     * This method is used to handle the currency convertions, if necessary.
     * In case the currency code is NOT "GBP" (which is the code corresponding to
     * the United Kingdom pounds), then this methos will consume a rest endpoint to
     * get the official convertion rates and actually convert the expenseAmount property
     * to the currency code stored in currencyCode.
     */
    public void currencyCalculations(){
        if (!this.currency.getCurrencyCode().equals("GBP")) {
            // if the amount is not specified in pounds, then it must be converted
            this.expenseAmount = CurrencyService.convert(this.expenseAmount, "GBP");
            // Vat calculation
            this.expenseAmountVAT = calcExpenseAmountVAT();
        }
    }

    public ExpenseDaoBean(){
        // Do not remove (used from ExpenseDAO interface)
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public String getExpenseDateString(DateTimeFormatter formatter) {
        return expenseDate.format(formatter);
    }

    public BigDecimal getExpenseAmount() {
        return expenseAmount;
    }

    public BigDecimal getExpenseAmountVAT() {
        return expenseAmountVAT;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getExpenseReason() {
        return expenseReason;
    }

    public int getId() {
        return id;
    }

    public String getExpenseUser() {
        // Do not remove (used from ExpenseDAO interface)
        return expenseUser;
    }

    public Currency getCurrency() {
        return currency;
    }

    private BigDecimal calcExpenseAmountVAT(){
        BigDecimal vat = GeneralSettings.getVAT().divide(BigDecimal.valueOf(100));
        return expenseAmount.multiply(vat);
    }
}