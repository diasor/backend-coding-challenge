package com.engage.codetest.dao;

import com.engage.codetest.api.GeneralSettings;
import com.engage.codetest.services.CurrencyService;
import com.engage.codetest.services.MoneyUtil;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Currency;

/**
 * @author Diana Sormani
 * Created: January 28, 2018
 * Last Updated: February 06, 2018
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
        this.currencyCode = currencyCode;
        this.currency = Currency.getInstance(this.currencyCode);
        this.expenseReason = expenseReason;
        this.expenseUser = expenseUser;

        this.expenseAmount = MoneyUtil.roundAmountToCurrency(expenseAmount, this.currency);
        this.expenseAmountVAT = MoneyUtil.roundAmountToCurrency(expenseAmountVAT, this.currency);

        // This constructor DOES NOT calculate vat amounts or currency rates because is only called when obtaining
        // expenses from the database (that is why the Expense already has an ID)
    }

    public ExpenseDaoBean(LocalDate expenseDate, BigDecimal expenseAmount, BigDecimal expenseAmountVAT, String currencyCode, String expenseReason, String user) {
        this.expenseDate = expenseDate;
        this.currencyCode = currencyCode;
        this.currency = Currency.getInstance(this.currencyCode);
        this.expenseReason = expenseReason;
        this.expenseUser = user;
        this.expenseAmount = MoneyUtil.roundAmountToCurrency(expenseAmount, this.currency);
        this.expenseAmountVAT = MoneyUtil.roundAmountToCurrency(expenseAmountVAT, this.currency);
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
        return this.expenseAmount;
    }

    public BigDecimal getExpenseAmountVAT() {
        return this.expenseAmountVAT;
    }

    public String getCurrencyCode() {
        return this.currencyCode;
    }

    public String getExpenseReason() {
        return this.expenseReason;
    }

    public int getId() {
        return this.id;
    }

    public String getExpenseUser() {
        // Do not remove (used from ExpenseDAO interface)
        return this.expenseUser;
    }

    public Currency getCurrency() {
        return currency;
    }

}