package com.engage.codetest.dao;

import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import java.util.List;

/**
 * @author Diana Sormani
 * Created: January 28, 2018
 * Last Updated: February 05, 2018
 * Description: This interface is used to interact with the RDBMS. It provides the required SQL queries
 *              to create and retrieve the records from the "expense" table.
*/
@RegisterMapper(ExpenseMapper.class)
public interface ExpenseDAO {

    /**
     * This method returns a list of the user's Expenses.
     */
    @SqlQuery(value = "select id, expense_date, expense_amount, expense_amount_VAT, amount_currency_ISO, expense_reason, expense_user " +
            "from expenses where expense_user = :user;")
    List<ExpenseDaoBean> getMyExpenses(@Bind("user") final String user);

    /**
     * This method returns ONE ExpenseJSON identified by id.
     */
    @SqlQuery("select id, expense_date, expense_amount, expense_amount_VAT, amount_currency_ISO, expense_reason, expense_user " +
            "from expenses where id = :id")
    ExpenseDaoBean getExpense(@Bind("id") final int id);

    /**
     * This method creates a new record on the expense table with the following information:
     *      - the date of the expense
     *      - the amount
     *      - the VAT amount
     *      - and the reason
     *      - the user
     *  Once the RDBMS creates the record, the id is obtained and stored in the ExpenseDaoBean.
     */
    @SqlUpdate("insert into expenses(expense_date, expense_amount, expense_amount_VAT, amount_currency_ISO, expense_reason, " +
            "expense_user) values(:expenseDate, :expenseAmount, :expenseAmountVAT, :currencyCode, :expenseReason, :expenseUser);")
    @GetGeneratedKeys
    int createExpense(@BindBean final ExpenseDaoBean exp);

    /**
     * This method is used for HealthCheck. It performs a select query over the "expenses" table
     * to check:
     *      - if the table exists
     *      - if the columns required exist
     */
    @SqlQuery("select id, expense_date, expense_amount, expense_amount_VAT, amount_currency_ISO, expense_reason, expense_user " +
            "from expenses limit 1;")
    List<ExpenseDaoBean> getOneExpenseCheck();
}