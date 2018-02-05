package com.engage.codetest.dao;

import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import java.util.List;

@RegisterMapper(ExpenseMapper.class)
public interface ExpenseDAO {

    /**
     * This method returns a list of the user's Expenses.
     */
    @SqlQuery(value = "select id, expense_date, expense_amount, expense_amount_VAT, amount_currency_ISO, expense_reason, expense_user from expenses where expense_user = :user;")
    // @RegisterBeanMapper(ExpenseDaoBean.class)
    List<ExpenseDaoBean> getMyExpenses( @Bind("user") final String user);

    /**
     * This method returns ONE ExpenseJSON identified by id.
     */
    @SqlQuery("select id, expense_date, expense_amount, expense_amount_VAT, amount_currency_ISO, expense_reason, expense_user from expenses where id = :id")
    ExpenseDaoBean getExpense(@Bind("id") final int id);

    /** todo arreglar comentario
     * This method creates a new ExpenseJSON given:
     *      - the date of the ExpenseJSON
     *      - the amount
     *      - the VAT amount
     *      - and the reason
     *      - the user
     *  Once the RDBMS creates the record, the id is obtained and stored in the ExpenseDaoBean.
     */
    @SqlUpdate("insert into expenses(expense_date, expense_amount, expense_amount_VAT, amount_currency_ISO, expense_reason, expense_user) values(:expenseDate, :expenseAmount, :expenseAmountVAT, :currencyCode, :expenseReason, :expenseUser);")
    @GetGeneratedKeys
    int createExpense(@BindBean final ExpenseDaoBean exp);
}