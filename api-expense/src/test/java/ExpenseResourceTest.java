import com.engage.codetest.api.ApiResult;
import com.engage.codetest.api.ErrorResult;
import com.engage.codetest.api.ExpenseJSON;
import com.engage.codetest.api.ExpenseResource;
import com.engage.codetest.dao.ExpenseDAO;
import com.engage.codetest.dao.ExpenseDaoBean;
import com.engage.codetest.security.BasicUser;
import com.engage.codetest.security.UserAuthenticator;
import com.engage.codetest.security.UserAuthorizer;
import com.engage.codetest.services.ExpenseService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import org.junit.Test;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Diana Sormani
 * Created: February 07, 2018
 * Last Updated: February 11, 2018
 * Description: The ExpenseResourcTester class implements tests with mockito for different scenarios:
 *          1) This case tests connection with the api /expenses/ via GET and checks that
 *          the return status is 200.
 *          2) This case tests the api /expenses/{id} via GET with a VALID id and checks
 *          whether it returns an ApiResult<ExpenseJSON> with the requested element and its staus.
 *          3) This case tests the api /expenses/{id} via GET with an INVALID id and checks
 *          whether it returns an ApiResult<ExpenseJSON> with an specific type of error.
 *          4) This case tests the api /expenses/ via POST for creating an expense with all the
 *          correct information.
 *          5) This case tests the rest api /expenses via GET for a specific user and checks the
 *          number of expenses returned as well as the isOk flag.
 *          6) This case tests the rest api /expenses via GET for a specific user that has NO expenses
 *          and checks the number of expenses returned (should be ZERO) as well as the isOk flag.
 *          7) This case tests the api /expenses/ via POST for creating an expense with a negative amount.
 *          8) This case tests the api /expenses/ via POST for creating an expense with a ZERO amount.
 *          9) This method tests the api /expenses/ via POST for creating an expense with an invalid amount
 *          which contains characters instead of just digits).
 */
public class ExpenseResourceTest {
    private static final DBI dbiMock = mock(DBI.class);
    private static final Handle handleMock = mock(Handle.class);
    private static final ExpenseDAO daoMock = mock(ExpenseDAO.class);
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    @ClassRule
    public static ResourceTestRule resources = ResourceTestRule.builder()
            .setTestContainerFactory(new GrizzlyWebTestContainerFactory())
            .addProvider(new AuthDynamicFeature(
                    new BasicCredentialAuthFilter.Builder<BasicUser>()
                            .setAuthenticator(new UserAuthenticator())
                            .setAuthorizer(new UserAuthorizer())
                            .setRealm("BASIC-AUTH-REALM")
                            .buildAuthFilter()))
            .addProvider(RolesAllowedDynamicFeature.class)
            .addProvider(new AuthValueFactoryProvider.Binder<>(BasicUser.class))
            .setRegisterDefaultExceptionMappers(false)
            .addResource(new ExpenseResource())
            .build();

    // Auxiliary test data
    private final String user = "diana";
    private final String currency = "GBP";
    private final ExpenseJSON expense = createTestExpense();
    private final ExpenseJSON expenseNegAmount = createTestExpenseNegAmount();
    private final ExpenseJSON expenseZeroAmount = createTestExpenseZeroAmount();
    private final String stringInvalidAmount = createTestExpenseCharAmount();
    private final ExpenseDaoBean expDao = createTestDaoBean();
    private List<ExpenseDaoBean> expenseDaoList = createTestDaoListOneUser();
    private List<ExpenseJSON> expenseList = createTestListOneUser();

    @Before
    public void setup() {
        when(dbiMock.open()).thenReturn(handleMock);
        when(handleMock.attach(ExpenseDAO.class)).thenReturn(daoMock);

        doReturn(78).when(daoMock).createExpense(any(ExpenseDaoBean.class));
        doReturn(expDao).when(daoMock).getExpense(78);                          // id = 78 tested as a VALID id
        doReturn(null).when(daoMock).getExpense(79);                // id = 79 tested as an INVALID id
        doReturn(new ArrayList<>()).when(daoMock).getMyExpenses("empty");      // user = "empty"
        doReturn(expenseDaoList).when(daoMock).getMyExpenses(user);                  // user = "diana"
        doReturn(new ArrayList<>()).when(daoMock).getMyExpenses("anotherUser"); // user = "anotherUser"

        ExpenseService.setDbi(dbiMock);
    }

    @After
    public void tearDown(){
        // The mock we has to be reseted  after each test because of the
        // @ClassRule, or use a @Rule as mentioned below.
        reset(dbiMock);
        reset(handleMock);
        reset(daoMock);
    }

    /**
     * Test 1) This method tests the connection to the rest api /expenses and check whether the
     * GET request response with a status 200.
     */
    @Test
    public void testGetStatus() throws Exception {
        Response response = resources.getJerseyTest().target("/expenses").request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Basic ZW1wdHk6c2VjcmV0") // connection with "empty:secret" in base64
                .get();
        assertEquals(200, response.getStatus());
    }

    /**
     * Test 2) This method tests the api /expenses/{id} and checks whether it
     * returns an ApiResult<ExpenseJSON> object indicating an "isOK" flag
     * and an ExpenseJSON the expected element.
     */
    @Test
    public void testGetValidExpense() throws IOException {
        String resultStr = resources.getJerseyTest().target("/expenses/78").request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Basic ZGlhbmE6c2VjcmV0")
                .get(String.class);

        // Expected result: member isOk = true;
        //                  member .data is equal field by field with an ExpenseJSON object
        ApiResult<ExpenseJSON> apiResultObject = MAPPER.readValue(resultStr, new TypeReference<ApiResult<ExpenseJSON>>(){});
        assertEquals(apiResultObject.getIsOk(), true);
        assertThat(apiResultObject.getData()).isEqualToComparingFieldByField(expense);
    }


    /**
     * Test 3) This method tests the api /expenses/{id} with an ID that does not exists (INVALID).
     * It checks that it returns an ApiResult<ExpenseJSON> object indicating an "isOK" flag
     * in false and an emptu list.
     */
    @Test
    public void testGetInvalidExpense() throws IOException {
        String resultStr = resources.getJerseyTest().target("/expenses/79").request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Basic ZGlhbmE6c2VjcmV0")
                .get(String.class);

        // Expected result: member isOk = false;
        //                  member .data = null
        //                  member .error = "The Object identified with: 79 was not found",
        //                                  "errorCode":"NOT_FOUND"
        ApiResult<ExpenseJSON> apiResultObject = MAPPER.readValue(resultStr, new TypeReference<ApiResult<ExpenseJSON>>(){});
        assertEquals(apiResultObject.getIsOk(), false);
        assertThat(apiResultObject.getData()).isNull();
        ErrorResult expenseNotExistError = new ErrorResult("The Object identified with: 79 was not found", "NOT_FOUND");
        assertThat(apiResultObject.getError()).isEqualToComparingFieldByField(expenseNotExistError);
    }

    /**
     * Test 4) This method tests the api /expenses/ via POST for creating an expense with all the
     * correct information.
     */
    @Test
    public void testCreateExpense() throws IOException {
        // The post is sent with an Entity storing the new expense information
        String resultStr = resources.getJerseyTest().target("/expenses/").request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Basic ZGlhbmE6c2VjcmV0")
                .post(Entity.json(MAPPER.writeValueAsString(expense)), String.class);

        // Expected result: member isOk = true;
        //                  member .data = member .data is equal field by field with an ExpenseJSON object
        ApiResult<ExpenseJSON> apiResultObject = MAPPER.readValue(resultStr, new TypeReference<ApiResult<ExpenseJSON>>(){});
        assertEquals(apiResultObject.getIsOk(), true);
        assertThat(apiResultObject.getData()).isEqualToComparingFieldByField(expense);
    }

    /**
     * Test 5) This method tests the rest api /expenses for a specific user and checks the
     * number of expenses returned as well as the isOk flag.
     */
    @Test
    public void testGetUserExpenses() throws Exception {
        String resultStr = resources.getJerseyTest().target("/expenses/").request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Basic ZGlhbmE6c2VjcmV0")  // connection with "diana:secret" in base64
                .get(String.class);

        // Expected result: member isOk = true;
        //                  member .data a list of expenses (List<ExpenseJSON>) of size 3
        ApiResult<List<ExpenseJSON>> apiResultObject = MAPPER.readValue(resultStr, new TypeReference<ApiResult<List<ExpenseJSON>>>(){});
        assertEquals(apiResultObject.getIsOk(), true);
        assertThat(apiResultObject.getData().size()).isEqualTo(3);
        //List<ExpenseJSON> expListUser = apiResultObject.getData();
    }

    /**
     * Test 6) This method tests the rest api /expenses for a specific user that has NO expenses
     * and checks the number of expenses returned (should be ZERO) as well as the isOk flag.
     */
    @Test
    public void testGetUserWithoutExpenses() throws Exception {
        String resultStr = resources.getJerseyTest().target("/expenses/").request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Basic YW5vdGhlclVzZXI6c2VjcmV0")  // connection with "anotherUser:secret" in base64
                .get(String.class);

        // Expected result: member isOk = true;
        //                  member .data an empty list (List<ExpenseJSON>)
        ApiResult<List<ExpenseJSON>> apiResultObject = MAPPER.readValue(resultStr, new TypeReference<ApiResult<List<ExpenseJSON>>>(){});
        assertEquals(apiResultObject.getIsOk(), true);
        assertThat(apiResultObject.getData().size()).isEqualTo(0);
    }

    /**
     * Test 7) This method tests the api /expenses/ via POST for creating an expense with a negative amount.
     */
    @Test
    public void testCreateExpenseNegAmount() throws IOException {
        // The post is sent with an Entity storing the new expense information
        String resultStr = resources.getJerseyTest().target("/expenses/").request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Basic ZGlhbmE6c2VjcmV0")
                .post(Entity.json(MAPPER.writeValueAsString(expenseNegAmount)), String.class);

        // Expected result: member isOk = false;
        //                  member .data = null;
        //                  error: Only positive expenses are allowed.
        ApiResult<ExpenseJSON> apiResultObject = MAPPER.readValue(resultStr, new TypeReference<ApiResult<ExpenseJSON>>(){});
        assertEquals(apiResultObject.getIsOk(), false);
        assertThat(apiResultObject.getData()).isNull();
        ErrorResult expenseNotExistError = new ErrorResult("Only positive expenses are allowed.", "INVALID_AMOUNT");
        assertThat(apiResultObject.getError()).isEqualToComparingFieldByField(expenseNotExistError);
    }

    /**
     * Test 8) This method tests the api /expenses/ via POST for creating an expense with a ZERO amount.
     */
    @Test
    public void testCreateExpenseZeroAmount() throws IOException {
        // The post is sent with an Entity storing the new expense information
        String resultStr = resources.getJerseyTest().target("/expenses/").request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Basic ZGlhbmE6c2VjcmV0")
                .post(Entity.json(MAPPER.writeValueAsString(expenseZeroAmount)), String.class);

        // Expected result: member isOk = false;
        //                  member .data = null;
        //                  error: Only positive expenses are allowed.
        ApiResult<ExpenseJSON> apiResultObject = MAPPER.readValue(resultStr, new TypeReference<ApiResult<ExpenseJSON>>(){});
        assertEquals(apiResultObject.getIsOk(), false);
        assertThat(apiResultObject.getData()).isNull();
        ErrorResult expenseNotExistError = new ErrorResult("Only positive expenses are allowed.", "INVALID_AMOUNT");
        assertThat(apiResultObject.getError()).isEqualToComparingFieldByField(expenseNotExistError);
    }

    /**
     * Test 9) This method tests the api /expenses/ via POST for creating an expense with an invalid amount
     *          which contains characters instead of just digits)..
     */
    @Test
    public void testCreateExpenseCharAmount() throws IOException {
        // The post is sent with an Entity storing the new expense information
        String resultStr = resources.getJerseyTest().target("/expenses/").request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Basic ZGlhbmE6c2VjcmV0")
                .post(Entity.json(stringInvalidAmount), String.class);

        // Expected result: member isOk = false;
        //                  member .data = null;
        //                  error: Only positive expenses are allowed.
        ApiResult<ExpenseJSON> apiResultObject = MAPPER.readValue(resultStr, new TypeReference<ApiResult<ExpenseJSON>>(){});
        assertEquals(apiResultObject.getIsOk(), false);
        assertThat(apiResultObject.getData()).isNull();
        ErrorResult expenseNotExistError = new ErrorResult("Character d is neither a decimal digit number, decimal point, nor \"e\" notation exponential mark.", "INVALID_AMOUNT");
        assertThat(apiResultObject.getError()).isEqualToComparingFieldByField(expenseNotExistError);
    }

    /**
     * Auxiliary methods for initializing data for testing
     */
    private ExpenseDaoBean createTestDaoBean(){
        LocalDate expLocalDate;
        {
            expLocalDate = LocalDate.of(2016, 01, 20);
        }
        return new ExpenseDaoBean(78, expLocalDate, BigDecimal.valueOf(1000), BigDecimal.valueOf(200).setScale(2), currency, "Expense created for testing purposes", user);
    }

    private ExpenseJSON createTestExpense(){
        // This method creates an expense with valid information
        return new ExpenseJSON(78, "20/01/2016", "1000.00", BigDecimal.valueOf(200).setScale(2), currency, "£", "Expense created for testing purposes", user);
    }

    private ExpenseJSON createTestExpenseNegAmount(){
        // This method creates an expense with a negative amount
        return new ExpenseJSON(0, "20/01/2026", "-1000.00", BigDecimal.valueOf(200).setScale(2), currency, "£", "Expense with negative amount", user);
    }

    private ExpenseJSON createTestExpenseZeroAmount(){
        // This method creates an expense with amount = 0
        return new ExpenseJSON(0, "20/01/2026", "0", BigDecimal.valueOf(200), currency, "£", "Expense with ZERO amount", user);
    }

    private String createTestExpenseCharAmount(){
        // This method creates a string with an expense with an input amount that has characters (not a valid number)

        return "{ \n" +
                "\"date\": \"01/01/2025\", \n" +
                "\"amount\": \"10dd\",\n" +
                "\"currency\" : \"GBP\" , \n" +
                "\"reason\" : \"Test with an amount containing characters (invalid number)\" , \n" +
                "\"user\" :\"diana\" \n" +
                "}";
    }

    private List<ExpenseJSON> createTestListOneUser() {
        // This method creates a List<ExpenseJSON> from a List<ExpenseDaoBean>
        DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        expenseList = expenseDaoList.stream()
                .map(e -> new ExpenseJSON(e.getId(), e.getExpenseDateString(DATE_FORMATTER), e.getExpenseAmount().toString(), e.getExpenseAmountVAT(), e.getCurrencyCode(), e.getCurrency().getSymbol(), e.getExpenseReason(), e.getExpenseUser()))
                .collect(Collectors.toList());

        return expenseList;
    }

    private List<ExpenseDaoBean> createTestDaoListOneUser() {
        expenseDaoList = new ArrayList<>();
        LocalDate expLocalDate;
        {
            expLocalDate = LocalDate.of(2016, 01, 20);
        }
        ExpenseDaoBean expDaoElem = new ExpenseDaoBean(80, expLocalDate, BigDecimal.valueOf(1000), BigDecimal.valueOf(200), currency, "First expense of the year", user);
        expenseDaoList.add(expDaoElem);
        expDaoElem = new ExpenseDaoBean(81, expLocalDate, BigDecimal.valueOf(2000), BigDecimal.valueOf(400), currency, "Second expense of the year", user);
        expenseDaoList.add(expDaoElem);
        expDaoElem = new ExpenseDaoBean(82, expLocalDate, BigDecimal.valueOf(3000), BigDecimal.valueOf(600), currency, "Third expense of the year", user);
        expenseDaoList.add(expDaoElem);

        return expenseDaoList;
    }

}
