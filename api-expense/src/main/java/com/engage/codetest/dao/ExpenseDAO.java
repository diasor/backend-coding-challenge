package com.engage.codetest.dao;

import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import java.util.List;

@RegisterMapper(ExpenseMapper.class)
public interface ExpenseDAO {

    // TODO filter by user
    /**
     * This method returns a list of the user's Expenses.
     * */
    @SqlQuery("select id, expense_date, expense_amount, expense_amount_VAT, amount_currency_ISO, expense_reason from expenses;")
    // @RegisterBeanMapper(ExpenseDaoBean.class)
    public List<ExpenseDaoBean> getMyExpenses();

    /**
     * This method returns ONE Expense identified by id.
     * */
    @SqlQuery("select id, expense_date, expense_amount, expense_amount_VAT, amount_currency_ISO, expense_reason from expenses where id = :id")
    public ExpenseDaoBean getExpense(@Bind("id") final int id);

    /**
     * This method creates a new Expense given:
     *      - the date of the Expense
     *      - the amount
     *      - the VAT amount
     *      - and the reason
     *  Once the RDBMS creates the record, the id is obtained and stored in the ExpenseDaoBean.
     * */
    @SqlUpdate("insert into expenses(expense_date, expense_amount, expense_amount_VAT, amount_currency_ISO, expense_reason) values(:expenseDate, :expenseAmount, :expenseAmountVAT, :currencyCode, :expenseReason);")
    @GetGeneratedKeys
    public int createExpense(@BindBean final ExpenseDaoBean exp);

    // TODO delete
    @SqlQuery("select last_insert_id();")
    public int lastInsertId();
}