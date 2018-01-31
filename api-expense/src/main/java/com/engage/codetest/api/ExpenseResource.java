package com.engage.codetest.api;

import com.codahale.metrics.annotation.Timed;
import com.engage.codetest.dao.ExpenseDaoBean;
import com.engage.codetest.services.ExpenseService;
import com.engage.codetest.services.ExpenseServiceBean;
import com.engage.codetest.services.ObjectNotFoundException;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Path("/expenses")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("ADMIN") // todo check roles + auth
public class ExpenseResource {
    public ExpenseResource() {
    }

    /**
     * This service returns ONE Expense identified by id. If the Expense does not exist, then
     * it captures an exception that is process and returned as part of the ApiResult object.
     * */
    @GET
    @Timed
    public ApiResult<List<ExpenseDaoBean>> getExpenses() {
        try{
           //return new ApiResult<List<ExpenseDaoBean>>(ExpenseService.getMyExpenses());
           return new ApiResult<>(ExpenseService.getMyExpenses());
        }
        catch (ObjectNotFoundException ex){
            return new ApiResult<>(ex.getDescription(), ex.getErrorCode());
        }
    }

    /**
     * This service returns ONE Expense identified by id. If the Expense does not exist, then
     * it returns an exception that is process and returned as part of the ApiResult object.
     * */
    @GET
    @Timed
    @Path("/{id}")
    public ApiResult<ExpenseDaoBean> getExpense(@PathParam("id") final int id) {
        try {
            return new ApiResult<>(ExpenseService.getExpense(id));
        } catch (ObjectNotFoundException ex) {
            return new ApiResult<>(ex.getDescription(), ex.getErrorCode());
        }
    }

    /**
     * This service creates a new Expense given:
     *      - the date of the Expense
     *      - the amount
     *      - the VAT amount
     *      - and the reason
     *  Once the RDBMS creates the record, the id is obtained and stored in the ExpenseDaoBean.
     *  If for some reason, the Expense could not be created, the service returns an exception
     *  that is process and returned as part of the ApiResult object.
     * */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Timed
    public ApiResult<ExpenseDaoBean> createExpense(@FormParam("date") final String expenseStrDate, @FormParam("amount") final BigDecimal expenseAmount,
                                                   @FormParam("currency") final short expenseCurr, @FormParam("reason") final String expenseReason) throws InvocationTargetException, IllegalAccessException {

        /** An ExpenseDaoBean object is created with the information obtained from the body of the HMTL
         * Which is expected with the following format:
         *  {
         *      date: 25/01/2018
         *      amount: 200.5
         *      currency: 784
         *      reason: This is the reason for the expense
         *  }
        */
        if (!expenseStrDate.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate expenseDate = LocalDate.parse(expenseStrDate, formatter);
            ExpenseServiceBean exp = new ExpenseServiceBean(expenseDate, expenseAmount, expenseCurr, expenseReason);
            try {
                return new ApiResult<>(ExpenseService.createExpense(exp));
            } catch (ObjectNotFoundException ex) {
                return new ApiResult<>(ex.getDescription(), ex.getErrorCode());
            }
        }
        else{
            return new ApiResult<>("The date can not be empty for a new expense.", "");
        }
    }
}