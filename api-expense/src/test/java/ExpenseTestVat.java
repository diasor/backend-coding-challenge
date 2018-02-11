import com.engage.codetest.api.GeneralSettings;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

/**
 * @author Diana Sormani
 * Created: February 08, 2018
 * Last Updated: February 10, 2018
 * Description: This class implements unit tests for vat retrieval and calculations.
 */
public class ExpenseTestVat {

    /**
     * Test 1) This method tests getting the vat from the general setting class
     */
    @Test
    public void testGetVatSetting() {
        BigDecimal expected = BigDecimal.valueOf(20);
        //BigDecimal vat = GeneralSettings.getVAT().divide(BigDecimal.valueOf(100));
        BigDecimal vat = GeneralSettings.getVAT();
        assertEquals(vat, expected);
    }

    /**
     * Test 2) This method calculates the Vat percentage
     */
    @Test
    public void testGetVatPge() {
        BigDecimal expected = BigDecimal.valueOf(0.20);
        BigDecimal vat = GeneralSettings.getVAT().divide(BigDecimal.valueOf(100));
        assertEquals(vat, expected);
    }

    /**
     * Test 3) This method tests the vat amount calculation with positive, negative and zero amount
     */
    @Test
    public void testCalculatVat() {
        BigDecimal expectedPge = BigDecimal.valueOf(0.20);

        // Positive amount
        BigDecimal amount = BigDecimal.valueOf(1000);
        BigDecimal calculatedAmount = expectedPge.multiply(amount);
        BigDecimal expectedCalc = BigDecimal.valueOf(200.0);
        assertEquals("The test failed with a Positive amount", calculatedAmount.stripTrailingZeros(), expectedCalc.stripTrailingZeros());

        // Amount = ZERO
        amount = BigDecimal.ZERO;
        calculatedAmount = expectedPge.multiply(amount);
        expectedCalc = BigDecimal.ZERO;
        assertEquals("The test failed with amount = Zero", calculatedAmount.stripTrailingZeros(), expectedCalc.stripTrailingZeros());

        // Negative amount
        amount = BigDecimal.valueOf(-1000);
        calculatedAmount = expectedPge.multiply(amount);
        expectedCalc = BigDecimal.valueOf(-200.0);
        assertEquals("The test failed with a Negative amount", calculatedAmount.stripTrailingZeros(), expectedCalc.stripTrailingZeros());
    }
}
