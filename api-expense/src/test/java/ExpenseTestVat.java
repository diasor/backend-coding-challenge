import com.engage.codetest.api.GeneralSettings;
import com.engage.codetest.services.ExpenseService;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
     * Test 3) This method tests the vat amount calculation with positive amount
     */
    @Test
    public void testCalculateVatPositive() {
        BigDecimal amount = BigDecimal.valueOf(1000);
        BigDecimal calculatedAmount = ExpenseService.calcVAT(amount);
        BigDecimal expectedCalc = BigDecimal.valueOf(200);
        assertEquals("The test failed with a Positive amount", calculatedAmount.compareTo(expectedCalc), 0);
    }

    /**
     * Test 4) This method tests the vat amount calculation with zero amount
     */
    @Test
    public void testCalculateVatZero() {
        BigDecimal amount = BigDecimal.ZERO;
        BigDecimal calculatedAmount = ExpenseService.calcVAT(amount);
        BigDecimal expectedCalc = BigDecimal.ZERO;
        assertEquals("The test failed with amount = Zero", calculatedAmount.compareTo(expectedCalc), 0);
    }

    /**
     * Test 5) This method tests the vat amount calculation with negative amount
     */
    @Test
    public void testCalculateVatNegative() {
        BigDecimal amount = BigDecimal.valueOf(-1000);
        BigDecimal calculatedAmount = ExpenseService.calcVAT(amount);
        BigDecimal expectedCalc = BigDecimal.valueOf(-200.0);
        assertEquals("The test failed with a Negative amount", calculatedAmount.compareTo(expectedCalc), 0);
    }
}
