import com.engage.codetest.api.ExpenseJSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Diana Sormani
 * Created: February 07, 2018
 * Last Updated: February 08, 2018
 * Description: The ExpenseTester class implements tests for serialization and deserialization of
 *              ExpenseJSON objects.
 */
public class ExpenseTester {
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    @Test
    public void serializesToJSON() throws Exception {
        // Testing serialization of an ExpenseJSON object
        int newId = 78;
        String date = "01/01/2018";
        BigDecimal amount = BigDecimal.valueOf(1000);
        BigDecimal vat = BigDecimal.valueOf(200);
        String currency = "GBP";
        String symbol = "£";
        String reason = "This is a serialization test";
        String user = "diana";
        final ExpenseJSON expense = new ExpenseJSON(newId, date, amount.toString(), vat, currency, symbol, reason, user);
        String expenseExample = "{ \n" +
                "\"id\": \"78\", \n" +
                "\"date\": \"01/01/2018\", \n" +
                "\"amount\": \"1000\",\n" +
                "\"vat\": \"200\", \n" +
                "\"currency\" : \"GBP\" , \n" +
                "\"currencySymbol\" : \"£\" , \n" +
                "\"reason\" : \"This is a serialization test\" , \n" +
                "\"user\" :\"diana\" \n" +
                "}";
        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(expenseExample, ExpenseJSON.class));

        assertThat(MAPPER.writeValueAsString(expense)).isEqualTo(expected);
    }

    @Test
    public void deserializesFromJSON() throws Exception {
        // Testing deserialization of an ExpenseJSON object
        int newId = 78;
        String date = "01/01/2018";
        BigDecimal amount = BigDecimal.valueOf(1000);
        BigDecimal vat = BigDecimal.valueOf(200);
        String currency = "GBP";
        String symbol = "£";
        String reason = "This is a serialization test";
        String user = "diana";
        final ExpenseJSON expense = new ExpenseJSON(newId, date, amount.toString(), vat, currency, symbol, reason, user);

        String expenseExample = "{ \n" +
                "\"id\": \"78\", \n" +
                "\"date\": \"01/01/2018\", \n" +
                "\"amount\": \"1000\",\n" +
                "\"vat\": \"200\", \n" +
                "\"currency\" : \"GBP\" , \n" +
                "\"currencySymbol\" : \"£\" , \n" +
                "\"reason\" : \"This is a serialization test\" , \n" +
                "\"user\" :\"diana\" \n" +
                "}";

        assertThat(MAPPER.readValue(expenseExample, ExpenseJSON.class)).isEqualToComparingFieldByField(expense);
    }
}
