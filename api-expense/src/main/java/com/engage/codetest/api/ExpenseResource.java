package com.engage.codetest.api;

import com.codahale.metrics.annotation.Timed;
import com.engage.codetest.dao.ExpenseDaoBean;
import com.engage.codetest.security.BasicUser;
import com.engage.codetest.services.ExpenseService;
import com.engage.codetest.services.ObjectNotFoundException;
import io.dropwizard.auth.Auth;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Diana Sormani
 * Created: January 28, 2018
 * Last Updated: February 05, 2018
 * Description: The ExpenseResource class implements a rest endpoint that provides three services:
 *              1) getUserExpenses: provides a list of the logged user's expenses.
 *              2) getExpenseById: provides the information of a given expense, identified by id.
 *              3) createExpense: creates a new expense.
 *              All the information is returned via an ApiResult object which in turn provides
 *              the required application information, and an status transaction message.
 */

@Path("/expenses")
@Produces(MediaType.APPLICATION_JSON)
public class ExpenseResource {

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ExpenseResource() {}

    /**
     * This service implements a service that returns a list of the user's logged expenses.
     * The expenses information is contained in a ExpenseJSON object.
     * The method returns an ApiResult object containing:
     *      * a list of the ExpenseJSON objects with the user's expenses.
     *      * a status from the transaction itself: this will indicate whether the query was carried on
     *      with or without erros. In case of error, a message will be returned describing said error.
     *      * If an error occurs, then the service will return an empty list of ExpenseJSON within the ApiResult object.
     * It can only be accessed via an http GET request.
     * Example: https://localhost:8443/expenses/
     */
    @GET
    @Timed
    public ApiResult<List<ExpenseJSON>> getUserExpenses(@Auth BasicUser user) {
        try {
            List<ExpenseDaoBean> expenses = ExpenseService.getUserExpenses(user.getUserName());
            List<ExpenseJSON> outExpenses = expenses.stream()
                    .map(e -> new ExpenseJSON(e.getExpenseDateString(DATE_FORMATTER), e.getExpenseAmount(), e.getExpenseAmountVAT(), e.getCurrencyCode(), e.getCurrency().getSymbol(), e.getExpenseReason(), user.getUserName()))
                    .collect(Collectors.toList());
            return new ApiResult<>(outExpenses);
        }
        catch (ObjectNotFoundException ex){
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
    @Timed
    @Path("/{id}")
    public ApiResult<ExpenseDaoBean> getExpenseById(@PathParam("id") final int id) {
        try {
            return new ApiResult<>(ExpenseService.getExpenseById(id));
        } catch (ObjectNotFoundException ex) {
            return new ApiResult<>(ex.getDescription(), ex.getErrorCode());
        }
    }

    /**
     * This service creates a new expense record with the provided information obtained in the body onf
     * an html POST.
     * The method returns an ApiResult object containing one ExpenseJSON object with the information
     * provided by the POST, plus the information calculated in the application.
     *      - the expense ID
     *      - the date
     *      - the amount
     *      - the VAT amount
     *      - and the reason
     * If an error occurs, then the service will return the description of said error on the ApiResult object.
     * It can only be accessed via an http POST request.
     * Example: https://localhost:8443/expenses/
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public ApiResult<ExpenseDaoBean> createExpense(final ExpenseJSON expenseJSON, @Auth final BasicUser user) throws InvocationTargetException, IllegalAccessException {
        /** An ExpenseDaoBean object is created with the information obtained from the body of the HMTL
         * Which is expected with the following format:
         *  {
         *      date: 25/01/2018
         *      amount: 200.5
         *      currency: GBP
         *      reason: This is the reason for the expense
         *  }
        */
        LocalDate expenseDate = LocalDate.parse(expenseJSON.getDate(), DATE_FORMATTER);
        ExpenseDaoBean exp = new ExpenseDaoBean(expenseDate, expenseJSON.getAmount(), expenseJSON.getCurrency(), expenseJSON.getReason(), user.getUserName());
        try {
            return new ApiResult<>(ExpenseService.createExpense(exp));
        } catch (ObjectNotFoundException ex) {
            return new ApiResult<>(ex.getDescription(), ex.getErrorCode());
        }
    }
}