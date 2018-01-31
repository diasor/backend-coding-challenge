package com.engage.codetest.services;

import com.engage.codetest.dao.ExpenseDaoBean;
import com.engage.codetest.dao.ExpenseDAO;
import org.apache.commons.beanutils.BeanUtils;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.sqlobject.Transaction;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.List;

public final class ExpenseService {
    private static final String PART_NOT_FOUND = "Part id %s not found.";
    //private static final String DATABASE_REACH_ERROR =
    //        "Could not reach the MySQL database. The database may be down or there may be network connectivity issues. Details: ";
    //private static final String DATABASE_CONNECTION_ERROR =
    //        "Could not create a connection to the MySQL database. The database configurations are likely incorrect. Details: ";
    private static final String DATABASE_UNEXPECTED_ERROR =
            "Unexpected error occurred while attempting to reach the database. Details: ";
    //private static final String SUCCESS = "Success...";
    //private static final String UNEXPECTED_ERROR = "An unexpected error occurred while deleting part.";

    private static DBI dbi;
    public static void setDbi(DBI d) {
        dbi = d;
    }

    private ExpenseService() {
    }

    /**
     * This service returns a list of the user's Expenses.
     * */
    // todo revisar si el @Transaction asi
    // todo revisar si esto esta manejando bien los recursos (es decir si no tiene leak de la conexion)
    // todo errores (i.e. terminar devolviendo un ApiResult con isOk = false y el error adentro
    @Transaction
    public static List<ExpenseDaoBean> getMyExpenses() {
        try (Handle handle = dbi.open()) {
            ExpenseDAO dao = handle.attach(ExpenseDAO.class);
            return dao.getMyExpenses();
        }
    }

    /**
     * This method returns ONE Expense identified by id. If the Expense does not exist, then
     * it returns an exception that will be handled one level above.
     * */
    @Transaction
    public static ExpenseDaoBean getExpense(int id) {
        try (Handle handle = dbi.open()) {
            ExpenseDAO dao = handle.attach(ExpenseDAO.class);
            ExpenseDaoBean expDao = dao.getExpense(id);
            if (expDao == null) {
                throw new ObjectNotFoundException(id, "Expense");
            }
            return expDao;
        }
    }

    /**
     * This method creates a new Expense given:
     *      - the date of the Expense
     *      - the amount
     *      - and the reason
     *  Once the RDBMS creates the record, the id is obtained and stored in the ExpenseDaoBean.
     *  If for some reason, the Expense could not be created, the service returns an exception.
     * */
    @Transaction
    public static ExpenseDaoBean createExpense(ExpenseServiceBean exp) throws InvocationTargetException, IllegalAccessException {
        try (Handle handle = dbi.open()) {
            ExpenseDAO dao = handle.attach(ExpenseDAO.class);
            // The vat amount is calculated as the 20% of the expenseAmount
            BigDecimal expenseAmountVAT = exp.getExpenseAmount().multiply(BigDecimal.valueOf(0.2));
            // A new object ExpenseDaoBean is created with the exact information the database requires
            // This new object's information is a copy from the one obtained from the service layer
            // plus the vat amount
            ExpenseDaoBean expDao = new ExpenseDaoBean();
            BeanUtils.copyProperties(expDao, exp);
            expDao.setExpenseAmountVAT(expenseAmountVAT);

            // The record in the ""expense"" table is created
            int generatedId = dao.createExpense(expDao);
            if (expDao == null) {
                throw new ObjectNotFoundException(generatedId, "Expense");
            }
            else {
                // Once the record is generated, then ID is obtained and set to the ExpenseDao object
                expDao.setId(generatedId);
            }
            return expDao;
        }
    }

    public static String performHealthCheck() {
        return null;
//        try {
//            expensesDao()getMyExpenses();
//        } catch (UnableToObtainConnectionException ex) {
//            // TODO return checkUnableToObtainConnectionException(ex);
//            return DATABASE_UNEXPECTED_ERROR + ex.getCause().getLocalizedMessage();
//        } catch (UnableToExecuteStatementException ex) {
//            // TODO return checkUnableToExecuteStatementException(ex);
//            return DATABASE_UNEXPECTED_ERROR + ex.getCause().getLocalizedMessage();
//        } catch (Exception ex) {
//            return DATABASE_UNEXPECTED_ERROR + ex.getCause().getLocalizedMessage();
//        }
//        return null;
    }
}
