package com.engage.codetest.services;

import com.engage.codetest.ExpensesApplication;
import com.engage.codetest.api.GeneralSettings;
import com.engage.codetest.dao.ExpenseDaoBean;
import com.engage.codetest.dao.ExpenseDAO;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.exceptions.UnableToObtainConnectionException;
import org.skife.jdbi.v2.sqlobject.Transaction;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Diana Sormani
 * Created: January 28, 2018
 * Last Updated: February 05, 2018
 * Description: The ExpenseService class contains the logic associated with de Expense REST service
 *              manipulation.
 *              It was created to be used as a static class for the ExpenseResource class.
 *              It implements the following methods for the ExpenseResource to consume and public:
 *                  * getUserExpenses
 *                  * getExpenseById
 *                  * createExpense
 */
public final class ExpenseService {
    private static final String EMPTY_DATE = "An Expense can not be created with a null date.";
    private static final String EMPTY_AMOUNT = "An Expense can not be created with an amount with value ZERO.";
    private static final String EMPTY_REASON = "An Expense can not be created with an empty Reason.";
    public static final String INVALID_CURRENCY = "The accepted values for the currency field are: GBP, EUR or empty";
    private static final String DATABASE_UNEXPECTED_ERROR =
            "Unexpected error occurred while attempting to reach the database. Details: ";

    private static DBI dbi;
    private static final String DEFAULT_CURRENCY = "GBP";

    private ExpenseService() { }

    public static void setDbi(DBI d) {
        dbi = d;
    }

    /**
     * This method returns a list of the user's Expenses.
     */
    @Transaction
    public static List<ExpenseDaoBean> getUserExpenses(String user) {
        try (Handle handle = dbi.open()) {
            ExpenseDAO dao = handle.attach(ExpenseDAO.class);
            return dao.getMyExpenses(user);
        }
        catch (UnableToObtainConnectionException ex){
            String exceptionDsc = DATABASE_UNEXPECTED_ERROR + ex.getCause().getLocalizedMessage();
            throw new SetupException("DATABASE", exceptionDsc);
        }
    }

    /**
     * This method returns ONE ExpenseJSON identified by id. If the ExpenseJSON does not exist, then
     * it returns an exception that will be handled one level above.
     */
    @Transaction
    public static ExpenseDaoBean getExpenseById(int id) {
        try (Handle handle = dbi.open()) {
            ExpenseDAO dao = handle.attach(ExpenseDAO.class);
            ExpenseDaoBean expDao = dao.getExpense(id);
            if (expDao == null) {
                throw new ObjectNotFoundException(id, "getExpenseById failed");
            }
            return expDao;
        }
    }

    /**
     * This method creates a new ExpenseJSON given a ExpenseDaoBean object with at least the following fields:
     *      - the date of the expense
     *      - the amount
     *      - and the reason
     */
    @Transaction
    public static ExpenseDaoBean createExpense(LocalDate expenseDate, BigDecimal expenseAmount, String currencyCode, String expenseReason, String user)
            throws InvocationTargetException, IllegalAccessException {
        try (Handle handle = dbi.open()) {
            // The parameters are validated, and if they are correct then a ExpenseDaoBen object is build
            ExpenseDaoBean expDao = buildExpenseDaoBean(expenseDate, expenseAmount, currencyCode, expenseReason, user);

            ExpenseDAO dao = handle.attach(ExpenseDAO.class);
            // The record in the "expense" table is created
            int generatedId = dao.createExpense(expDao);
            // Once the record is generated, then ID is obtained and set to the ExpenseDao object
            expDao.setId(generatedId);
            // The information has been logged
            ExpensesApplication.expenseLogger.info("A new Expense has been created with ID: " + generatedId);
            return expDao;
        }
    }

    public static ExpenseDaoBean buildExpenseDaoBean(LocalDate expenseDate, BigDecimal expenseAmount, String currencyCode, String expenseReason, String user) {
        if (expenseDate == null){
            // An Expense can not be created with a null date
            throw new InvalidInputException("date", EMPTY_DATE);
        }
        if (expenseAmount.equals(BigDecimal.ZERO)){
            // An Expense can not be created with an Amount with value ZERO
            throw new InvalidInputException("amount", EMPTY_AMOUNT);
        }
        if ((expenseReason == null) || expenseReason.isEmpty()) {
            // An Expense can not be created with an empty Reason
            throw new InvalidInputException("reason", EMPTY_REASON);
        }
        /**
         * Currency controls:
         * The currency CAN NOT be null. If it is, then it will be set by default to GBP
         * If the currency code is EUR, the amounts will be translated to GBP anyway, so the
         * currency code will still be GPB.
          */
        // GBP is the default currency
        if ((currencyCode == null) || currencyCode.isEmpty()) {
            currencyCode = "GBP";
        }
        // Only EUR or GBP are accepted currencies for now.
        if ((!currencyCode.equals("GBP")) && (!currencyCode.equals("EUR"))) {
            // An Expense has only 3 valid values for the currency: GBP, EUR or empty.
            throw new InvalidInputException("currency", INVALID_CURRENCY);
        }

        // we always convert to GBP ( if necessary )
        if (!currencyCode.equals(DEFAULT_CURRENCY)){
            // If the amount is not specified in pounds, then the only other valid option is that it was
            // specified in EUR. Then the amount must be converted to GBP
            // todo control / create an exception if the convert fails
            expenseAmount = CurrencyService.convertEURToGBP(expenseAmount);
        }
        BigDecimal expenseAmountVAT = calcVAT(expenseAmount);

        // Once the input fields has been checked, the ExpenseDaoBean object is created
        return new ExpenseDaoBean(expenseDate, expenseAmount, expenseAmountVAT, DEFAULT_CURRENCY, expenseReason, user);
    }

    public static BigDecimal calcVAT(BigDecimal expenseAmount) {
        // VAT calculations
        BigDecimal vat = GeneralSettings.getVAT().divide(BigDecimal.valueOf(100));
        return expenseAmount.multiply(vat);
    }

    /**
     * This method performs health checks for the application. Particularly it checks:
     *      * If the connection to the database is on.
     *      * If the table expenses exists with the necessary columns.
     *      * If the conversion rest service is available.
     * @return a string containing the errors found, or null otherwise
     */
    public static String performHealthCheck() {
        try (Handle handle = dbi.open()) {
            ExpenseDAO dao = handle.attach(ExpenseDAO.class);
            dao.getOneExpenseCheck();
        } catch (Exception ex) {
            String exceptionString = DATABASE_UNEXPECTED_ERROR + ex.getCause().getLocalizedMessage();
            ExpensesApplication.expenseLogger.error(exceptionString);
            return exceptionString;
        }

        try {
            CurrencyService.convertCheck();
        }catch (SetupException ex) {
            String exceptionString = ex.getErrorCode() + " " +  ex.getDescription();
            ExpensesApplication.expenseLogger.error(exceptionString);
            return exceptionString;
        }
        return null;
    }
}
