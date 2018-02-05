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

@Path("/expenses")
@Produces(MediaType.APPLICATION_JSON)
public class ExpenseResource {

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ExpenseResource() {}

    /**
     * This service returns ONE ExpenseJSON identified by id. If the ExpenseJSON does not exist, then
     * it captures an exception that is process and returned as part of the ApiResult object.
     */
    @GET
    @Timed
    public ApiResult<List<ExpenseJSON>> getExpenses(@Auth BasicUser user) {
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
     * This service returns ONE ExpenseJSON identified by id. If the ExpenseJSON does not exist, then
     * it returns an exception that is process and returned as part of the ApiResult object.
     */
    @GET
    @Timed
    @Path("/{id}")
    public ApiResult<ExpenseDaoBean> getExpense(@Auth BasicUser user, @PathParam("id") final int id) {
        try {
            return new ApiResult<>(ExpenseService.getExpense(user.getUserName(), id));
        } catch (ObjectNotFoundException ex) {
            return new ApiResult<>(ex.getDescription(), ex.getErrorCode());
        }
    }

    /**
     * This service creates a new ExpenseJSON given:
     *      - the date of the ExpenseJSON
     *      - the amount
     *      - the VAT amount
     *      - and the reason
     *  Once the RDBMS creates the record, the id is obtained and stored in the ExpenseDaoBean.
     *  If for some reason, the ExpenseJSON could not be created, the service returns an exception
     *  that is process and returned as part of the ApiResult object.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
/*    public ApiResult<ExpenseDaoBean> createExpense(@FormParam("date") final String expenseStrDate, @FormParam("amount") final BigDecimal expenseAmount,
                                                   @FormParam("currency") final short expenseCurr, @FormParam("reason") final String expenseReason,
                                                   @Auth final BasicUser user) throws InvocationTargetException, IllegalAccessException {
*/
    public ApiResult<ExpenseDaoBean> createExpense(final ExpenseJSON expenseJSON, @Auth final BasicUser user) throws InvocationTargetException, IllegalAccessException {
        /** An ExpenseDaoBean object is created with the information obtained from the body of the HMTL
         * Which is expected with the following format:
         *  {
         *      date: 25/01/2018
         *      amount: 200.5
         *      currency: 784
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
        /*}
        else{
            return new ApiResult<>("The date can not be empty for a new expense.", "");
        }
        */
    }
}