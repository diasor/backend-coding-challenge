import com.engage.codetest.api.CurrencyData;
import com.engage.codetest.api.GeneralSettings;
import com.engage.codetest.dao.ExpenseDaoBean;
import com.engage.codetest.services.CurrencyService;
import com.engage.codetest.services.ExpenseService;
import com.engage.codetest.services.InvalidInputException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Invocation;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Diana Sormani
 * Created: February 08, 2018
 * Last Updated: February 10, 2018
 * Description: This class implements unit tests over the currency management.
 */
public class ExpenseTestCurrency {
    private static final String testApiURL = GeneralSettings.getApiURL();
    private static final String testApiKey = GeneralSettings.getApiKey();
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    private static Invocation.Builder currencyAPIMock = mock(Invocation.Builder.class);
    private static CurrencyData currencyData = new CurrencyData(true);

    @Before
    public void setup() {
        when(currencyAPIMock.get(CurrencyData.class)).thenReturn(currencyData);
        CurrencyService.setApiClient(currencyAPIMock);
        currencyData.quotes.put("USDEUR", BigDecimal.valueOf(0.10));
        currencyData.quotes.put("USDGBP", BigDecimal.valueOf(0.50));
    }

    /**
     * Test 1) This method tests that when creating a new ExpenseDaoBean object with the currency code value empty,
     * the system will fill currency code as GBP by default and the currency symbol as £
     * and calculate the vat according to the vat percentage.
     */
    @Test
    public void testExpenseWithoutCurrency() {
        BigDecimal amount = BigDecimal.valueOf(1000);
        String currency = "";   // empty currency code
        String reason = "Creating an expense without currency code.";
        String user = "diana";
        final LocalDate expLocalDate;
        {
            expLocalDate = LocalDate.of(2020, 12, 25);
        }

        // The expense object is created
        ExpenseDaoBean expDao = ExpenseService.buildExpenseDaoBean(expLocalDate, amount, currency, reason, user);

        // Expected expense: After its creation, the object should have currency code and symbol assigned.
        BigDecimal vat = BigDecimal.valueOf(200);
        String expectedCurrencyCode = "GBP"; // currency with United Kingdom pounds
        ExpenseDaoBean expDaoExpected = new ExpenseDaoBean(0, expLocalDate, amount, vat, expectedCurrencyCode, reason, user);

        assertThat(expDao).isEqualToComparingFieldByField(expDaoExpected);
    }

    /**
     * Test 2) This method tests that when creating a new ExpenseDaoBean object with the currency code value = GBP,
     * the system will fill currency symbol as £ and calculate the vat according to the vat
     * percentage.
     */
    @Test
    public void testExpenseWithGBPCurrency() {
        BigDecimal amount = BigDecimal.valueOf(4000);
        String currency = "GBP";   // Currency code = UK pounds
        String reason = "Creating an expense with GBP as currency code.";
        String user = "diana";
        final LocalDate expLocalDate;
        {
            expLocalDate = LocalDate.of(2020, 12, 25);
        }

        // The expense object is created
        ExpenseDaoBean expDao = ExpenseService.buildExpenseDaoBean(expLocalDate, amount, currency, reason, user);

        // Expected expense: After its creation, the object should have currency code and symbol assigned
        BigDecimal vat = BigDecimal.valueOf(800);
        String expectedCurrencyCode = "GBP";
        ExpenseDaoBean expDaoExpected = new ExpenseDaoBean(0, expLocalDate, amount, vat, expectedCurrencyCode, reason, user);

        assertThat(expDao).isEqualToComparingFieldByField(expDaoExpected);
    }

    /**
     * Test 3) This method tests that when creating a new ExpenseDaoBean object with the c`urrency code value = EUR,
     * the system will fill currency symbol as and calculate the vat according to the vat
     * percentage.
     */
    @Test
    public void testExpenseWithEURCurrency() throws IOException {
        BigDecimal amount = BigDecimal.valueOf(1000);
        String currency = "EUR";   // Currency code = YEN
        String reason = "Creating an expense with YEN as currency code.";
        String user = "diana";
        final LocalDate expLocalDate;
        {
            expLocalDate = LocalDate.of(2020, 12, 25);
        }

        // The expense object is created
        ExpenseDaoBean expDao = ExpenseService.buildExpenseDaoBean(expLocalDate, amount, currency, reason, user);

        // Expected expense: After its creation, the object should have currency code and symbol assigned
        String expectedCurrencyCode = "GBP";
        BigDecimal convertedAmount = BigDecimal.valueOf(5000);
        BigDecimal convertedVat = BigDecimal.valueOf(1000);
        ExpenseDaoBean expDaoExpected = new ExpenseDaoBean(0, expLocalDate, convertedAmount, convertedVat, expectedCurrencyCode, reason, user);

        assertThat(expDao).isEqualToComparingFieldByField(expDaoExpected);
    }

    /**
     * Test 4) This method tests that when creating a new ExpenseDaoBean object with a currency code DIFFERENT than EUR,
     * GBP or empty, the system will throw an exception with code INVALID_CURRENCY and a clear description.
     */
    @Test
    public void testExpenseWithOtherCurrency() throws IOException {
        BigDecimal amount = BigDecimal.valueOf(1000);
        String currency = "YEN";   // Currency code = YEN
        String reason = "Creating an expense with YEN as currency code.";
        String user = "diana";
        final LocalDate expLocalDate;
        {
            expLocalDate = LocalDate.of(2020, 12, 25);
        }

        // Expected result: when attempting to create an expense with a currency different than
        // EUR, GBP or empty, then the system should throw an InvalidInputException
        // indicating that the error was de "INVALID_CURRENCY"
        String exceptionMessageExpected = "Input currency: " + ExpenseService.INVALID_CURRENCY;
        assertThatThrownBy(() -> {
            // The expense object is created
            ExpenseService.buildExpenseDaoBean(expLocalDate, amount, currency, reason, user);})
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining(exceptionMessageExpected);
    }
}
