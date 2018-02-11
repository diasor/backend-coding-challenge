package com.engage.codetest.api;

import com.codahale.metrics.annotation.Timed;
import com.engage.codetest.ExpensesApplication;
import com.engage.codetest.dao.ExpenseDaoBean;
import com.engage.codetest.security.BasicUser;
import com.engage.codetest.services.ExpenseService;
import com.engage.codetest.services.InvalidInputException;
import com.engage.codetest.services.ObjectNotFoundException;
import com.engage.codetest.services.SetupException;
import io.dropwizard.auth.Auth;
import io.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Diana Sormani
 * Created: January 28, 2018
 * Last Updated: February 11, 2018
 * Description: The ExpenseResource class implements a rest endpoint that provides three services:
 *      1) getUserExpenses: provides a list of the logged user's expenses.
 *      2) getExpenseById: provides the information of a given expense, identified by id.
 *      3) createExpense: creates a new expense.
 *      All the information is returned via an ApiResult object which in turn provides
 *      the required application information, and an status transaction message.
 *      This resource also provides documentation via Swagger for the front end application consumers.
 */

@Api(value = "/expenses", description = "Endpoint for Expenses")
@Path("/expenses")
@Produces(MediaType.APPLICATION_JSON)
public class ExpenseResource {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final String INVALID_DATE = "INVALID_DATE";
    private static final String INVALID_AMOUNT = "INVALID_AMOUNT";

    public ExpenseResource() {
    }

    /**
     * This service implements a service that returns a list of the user's logged expenses.
     * The expenses information is contained in a ExpenseJSON object.
     * The method returns an ApiResult object containing:
     *      - a list of the ExpenseJSON objects with the user's expenses.
     *      - a status from the transaction itself: this will indicate whether the query was carried on
     *          with or without erros. In case of error, a message will be returned describing said error.
     *      - If an error occurs, then the service will return an empty list of ExpenseJSON within the
     *      ApiResult object.
     *      The api can only be accessed via an http GET request.
     * Example: https://localhost:8443/expenses/
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    @ApiOperation(
            value = "Lists all expenses",
            notes = "Lists all expenses filtering by user"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of expenses"),
            @ApiResponse(code = 404, message = "Expense records not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ApiResult<List<ExpenseJSON>> getUserExpenses(@Auth BasicUser user) {
        if (user == null){
            new ApiResult<>("The loggin user can not be null.", "USER_NULL");
        }
        try {
            String logUser = user.getUserName();
            List<ExpenseDaoBean> expenses = ExpenseService.getUserExpenses(logUser);
            List<ExpenseJSON> outExpenses = expenses.stream()
                    .map(e -> new ExpenseJSON(e.getId(), e.getExpenseDateString(DATE_FORMATTER), e.getExpenseAmount().toString(), e.getExpenseAmountVAT(), e.getCurrencyCode(), e.getCurrency().getSymbol(), e.getExpenseReason(), e.getExpenseUser()))
                    .collect(Collectors.toList());
            return new ApiResult<>(outExpenses);
        } catch (ObjectNotFoundException ex) {
            String exceptionString = ex.getErrorCode() + " " + ex.getDescription();
            ExpensesApplication.expenseLogger.error(exceptionString);
            return new ApiResult<>(ex.getDescription(), ex.getErrorCode());
        } catch (SetupException ex) {
            String exceptionString = ex.getErrorCode() + " " + ex.getDescription();
            ExpensesApplication.expenseLogger.error(exceptionString);
            return new ApiResult<>(ex.getDescription(), ex.getErrorCode());
        }
    }

    /**
     * This service implements a service that returns ONE ExpenseJSON identified by id.
     * If the ExpenseJSON does not exist, then it returns an exception that is process and returned as part of the ApiResult object.
     * It can only be accessed via an http GET request, indicating the ID of thee expense.
     * Example: https://localhost:8443/expenses/2
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    @Path("/{id}")
    @ApiOperation(
            value = "Get an expense by ID",
            notes = "Returns the information of an expense identified by the parameter id.",
            response = ApiResult.class,
            responseContainer = "ApiResult"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of expenses"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ApiResult<ExpenseJSON> getExpenseById(@ApiParam(value = "Expense id", required = true) @PathParam("id") final int id) {
        try {
            ExpenseDaoBean expDao = ExpenseService.getExpenseById(id);
            ExpenseJSON expense = new ExpenseJSON(expDao.getId(), expDao.getExpenseDateString(DATE_FORMATTER), expDao.getExpenseAmount().toString(), expDao.getExpenseAmountVAT(), expDao.getCurrencyCode(), expDao.getCurrency().getSymbol(), expDao.getExpenseReason(), expDao.getExpenseUser());
            return new ApiResult<>(expense);
        } catch (ObjectNotFoundException ex) {
            String exceptionString = ex.getErrorCode() + " " + ex.getDescription();
            ExpensesApplication.expenseLogger.warn(exceptionString);
            return new ApiResult<>(ex.getDescription(), ex.getErrorCode());
        }
    }

    /**
     * This service creates a new expense record with the provided information obtained from an HTTP POST
     * form.
     * The method returns an ApiResult object containing one ExpenseJSON object with the information
     * provided by the POST, plus the information calculated in the application.
     * - the expense ID
     * - the date
     * - the amount
     * - the VAT amount
     * - and the reason
     * If an error occurs, then the service will return the description of said error on the ApiResult object.
     * It can only be accessed via an http POST request.
     * Example: https://localhost:8443/expenses/
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public ApiResult<ExpenseJSON> createExpense(final ExpenseJSON expenseJSON, @Auth final BasicUser user) throws InvocationTargetException, IllegalAccessException {
        /** An ExpenseJSON object is created with the information obtained from the body of the HTML
         * Which is expected with the following format:
         *  {
         *      date: 25/01/2018
         *      amount: 1000
         *      vat: 200
         *      currency: GBP
         *      currencySymbol:
         *      reason: This is the reason for the expense
         *  }
         */
        if (user == null){
            new ApiResult<>("The loggin user can not be null.", "USER_NULL");
        }
        try {
            // A Local Date is created based on the predefined DATE_FORMATTER
            LocalDate expenseDate = LocalDate.parse(expenseJSON.getDate(), DATE_FORMATTER);
            // If there was no error on the date, then the ExpenseService class is used to create a new Expense.
            // The result is returned in an ApiResult object
            BigDecimal amount = new BigDecimal(expenseJSON.getAmount());
            if (amount.compareTo(BigDecimal.ZERO) <= 0){
                // Only positive expenses are allowed
                return new ApiResult<>("Only positive expenses are allowed.", "INVALID_AMOUNT");
            }
            ExpenseDaoBean expDao = ExpenseService.createExpense(expenseDate, amount, expenseJSON.getCurrency(), expenseJSON.getReason(), user.getUserName());
            ExpenseJSON expense = new ExpenseJSON(expDao.getId(), expDao.getExpenseDateString(DATE_FORMATTER), expDao.getExpenseAmount().toString(), expDao.getExpenseAmountVAT(), expDao.getCurrencyCode(), expDao.getCurrency().getSymbol(), expDao.getExpenseReason(), expDao.getExpenseUser());
            return new ApiResult<>(expense);
        } catch (ObjectNotFoundException ex) {
            String exceptionString = ex.getErrorCode() + " " + ex.getDescription();
            ExpensesApplication.expenseLogger.error(exceptionString);
            return new ApiResult<>(ex.getDescription(), ex.getErrorCode());
        } catch (InvalidInputException ex) {
            String exceptionString = ex.getErrorCode() + " " + ex.getDescription();
            ExpensesApplication.expenseLogger.error(exceptionString);
            return new ApiResult<>(ex.getDescription(), ex.getErrorCode());
        } catch (DateTimeException ex) {
            // Exception created with the date
            String exceptionString = INVALID_DATE + " " + ex.getLocalizedMessage();
            ExpensesApplication.expenseLogger.error(exceptionString);
            return new ApiResult<>(ex.getLocalizedMessage(), INVALID_DATE);
        } catch (IllegalArgumentException ex) {
            String exCode = "";
            // Exception created with the date or an invalid amount
            if (ex.getLocalizedMessage().contains("digit number")){
                exCode = INVALID_AMOUNT;
                String exceptionString = INVALID_AMOUNT + " " + ex.getLocalizedMessage();
                ExpensesApplication.expenseLogger.error(exceptionString);
            }
            else {
                exCode = INVALID_DATE;
                String exceptionString = INVALID_DATE + " " + ex.getLocalizedMessage();
                ExpensesApplication.expenseLogger.error(exceptionString);
            }
            return new ApiResult<>(ex.getLocalizedMessage(), exCode);
        }
    }
}