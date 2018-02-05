package com.engage.codetest.services;

import com.engage.codetest.dao.ExpenseDaoBean;
import com.engage.codetest.dao.ExpenseDAO;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.sqlobject.Transaction;

import javax.ws.rs.client.Client;
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
    private static Client currencyClient;

    public static void setDbi(DBI d) {
        dbi = d;
    }

    public static void setCurrencyClient(Client client){
        currencyClient = client;
    }

    private ExpenseService() { }

    /**
     * This service returns a list of the user's Expenses.
     */
    // todo revisar si el @Transaction asi
    // todo revisar si esto esta manejando bien los recursos (es decir si no tiene leak de la conexion)
    // todo errores (i.e. terminar devolviendo un ApiResult con isOk = false y el error adentro
    @Transaction
    public static List<ExpenseDaoBean> getUserExpenses(String user) {
        try (Handle handle = dbi.open()) {
            ExpenseDAO dao = handle.attach(ExpenseDAO.class);
            return dao.getMyExpenses(user);
        }
    }

    /**
     * This method returns ONE ExpenseJSON identified by id. If the ExpenseJSON does not exist, then
     * it returns an exception that will be handled one level above.
     */
    // todo usar el user (ej. verificar que el expense sea del user, o tirar error)
    // todo o si no sacar el user
    @Transaction
    public static ExpenseDaoBean getExpense(String user, int id) {
        try (Handle handle = dbi.open()) {
            ExpenseDAO dao = handle.attach(ExpenseDAO.class);
            ExpenseDaoBean expDao = dao.getExpense(id);
            if (expDao == null) {
                throw new ObjectNotFoundException(id, "ExpenseJSON");
            }
            return expDao;
        }
    }

    /**
     * This method creates a new ExpenseJSON given:
     *      - the date of the ExpenseJSON
     *      - the amount
     *      - and the reason
     *  Once the RDBMS creates the record, the id is obtained and stored in the ExpenseDaoBean.
     *  If for some reason, the ExpenseJSON could not be created, the service returns an exception.
     */
    @Transaction
    public static ExpenseDaoBean createExpense(ExpenseDaoBean expDao) throws InvocationTargetException, IllegalAccessException {
        try (Handle handle = dbi.open()) {
            if (expDao.getExpenseDate() == null){
                // An Expense can not be created with a null date
                throw new ObjectInvalidInputException("date", "An Expense can not be created with a null date.");
            }
            if (expDao.getExpenseAmount().equals(BigDecimal.ZERO)){
                // An Expense can not be created with an Amount with value ZERO
                throw new ObjectInvalidInputException("amount", "An Expense can not be created with an amount with value ZERO.");
            }
            if (expDao.getExpenseReason().isEmpty()){
                // An Expense can not be created with an empty Reason
                throw new ObjectInvalidInputException("reason", "An Expense can not be created with an empty Reason.");
            }
            // currency
            System.out.println("currency " + expDao.getCurrency().getCurrencyCode());
            if (!expDao.getCurrencyCode().equals("GBP")) {
                expDao.currencyCalculations(currencyClient);
            }

            ExpenseDAO dao = handle.attach(ExpenseDAO.class);
            // todo fix comments, add log
            // The record in the "expense" table is created
            int generatedId = dao.createExpense(expDao);
            // Once the record is generated, then ID is obtained and set to the ExpenseDao object
            // todo log generated id
            expDao.setId(generatedId);
            return expDao;
        }
    }

    public static String performHealthCheck() {

//        try {
//            expensesDao()getUserExpenses();
//        } catch (UnableToObtainConnectionException ex) {
//            // TODO return checkUnableToObtainConnectionException(ex);
//            return DATABASE_UNEXPECTED_ERROR + ex.getCause().getLocalizedMessage();
//        } catch (UnableToExecuteStatementException ex) {
//            // TODO return checkUnableToExecuteStatementException(ex);
//            return DATABASE_UNEXPECTED_ERROR + ex.getCause().getLocalizedMessage();
//        } catch (Exception ex) {
//            return DATABASE_UNEXPECTED_ERROR + ex.getCause().getLocalizedMessage();
//        }
        return null;
    }
}
