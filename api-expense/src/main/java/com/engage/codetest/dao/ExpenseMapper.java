package com.engage.codetest.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author Diana Sormani
 * Created: January 28, 2018
 * Last Updated: February 05, 2018
 * Description: The ExpenseMapper class implements the mapping between the ExpenseDaoBean class,
 *              which contains all the data for a given Expense, and the ResultSet from
 *              the dadtabase.
 *              // todo verificar desc
 */
public class ExpenseMapper implements ResultSetMapper<ExpenseDaoBean>{
    private static final String ID = "id";
    private static final String DATE = "expense_date";
    private static final String AMOUNT = "expense_amount";
    private static final String AMOUNT_VAT = "expense_amount_VAT";
    private static final String CURRENCY = "amount_currency_ISO";
    private static final String REASON = "expense_reason";
    private static final String USER = "expense_user";

    public ExpenseDaoBean map(int i, ResultSet resultSet, StatementContext statementContext)
            throws SQLException {

        // todo turn into log
        /*
        System.out.println("Llega hasta MAPPER");
        System.out.println(resultSet.getInt(ID));
        System.out.println("************* DATE " + resultSet.getDate(DATE));
        System.out.println("************* AMOUNT " + resultSet.getBigDecimal(AMOUNT));
        System.out.println("************* VAT " + resultSet.getBigDecimal(AMOUNT_VAT));
        System.out.println("************* CURRENCY " + resultSet.getString(CURRENCY));
        System.out.println("************* REASON " + resultSet.getString(REASON));
        */
        return new ExpenseDaoBean(resultSet.getInt(ID), resultSet.getDate(DATE).toLocalDate(), resultSet.getBigDecimal(AMOUNT),
                resultSet.getBigDecimal(AMOUNT_VAT), resultSet.getString(CURRENCY), resultSet.getString(REASON), resultSet.getString(USER));
        }
}
