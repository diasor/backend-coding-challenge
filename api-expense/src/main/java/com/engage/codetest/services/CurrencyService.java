package com.engage.codetest.services;

import com.engage.codetest.ExpensesApplication;
import com.engage.codetest.api.CurrencyData;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Diana Sormani
 * Created: February 04, 2018
 * Last Updated: February 08, 2018
 * Description: The CurrencyService class implements the logic to obtain the conversion rates to United
 *              Kingdom pounds and to convertEURToGBP a given amount from EURO to GBP.
 */
public final class CurrencyService {
    private static Client client;            // A Jersey client to connect to external API to get exchange rate.
    private static String apiURL;            // The URL to access exchange rate API.
    private static String apiKey;            // The key to access exchange rate API.
    private static final String CONVERT_SERVICE_FAILURE = "The conversion rest service failed";

    private static Invocation.Builder apiClient;

    private CurrencyService(){}

    public static void setCurrencyData(Client curClient, String curApiURL, String curApiKey){
        client = curClient;
        apiURL = curApiURL;
        apiKey = curApiKey;

        setApiClient(client.target(apiURL)
                .queryParam("access_key", apiKey)
                // doesn't work with free plan .queryParam("source", currSource)
                .queryParam("currencies", "GBP,EUR")
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    public static void setApiClient(Invocation.Builder client) {
        apiClient = client;
    }

    public static BigDecimal convertEURToGBP(BigDecimal amountToConvert) {
        //Check that amount to convertEURToGBP is present.
        if (amountToConvert.equals(BigDecimal.ZERO)) {
            return amountToConvert;
        }

        CurrencyData currencyData = apiClient.get(CurrencyData.class);

        if (currencyData.getQuotes().size() == 0) {
            ExpensesApplication.expenseLogger.info("The currency converter rest api has not returned any conversion rate.");
        }
        // Get rates to convertEURToGBP o US dollars (that is the only conversion available for the free api
        BigDecimal fromRate = currencyData.getQuotes().get("USDEUR");
        BigDecimal toRate = currencyData.getQuotes().get("USDGBP");
        BigDecimal convertToUS = amountToConvert.divide(fromRate, 3, RoundingMode.CEILING);
        return convertToUS.multiply(toRate);
    }

    static void convertCheck() {
        try {
            CurrencyData currencyData = client
                    .target(apiURL)
                    .queryParam("access_key", apiKey)
                    // doesn't work with free plan .queryParam("source", currSource)
                    .queryParam("currencies", "GBP,EUR")
                    .request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .get(CurrencyData.class);
        }
        catch (Exception ex){
            throw new SetupException("CONVERT", CONVERT_SERVICE_FAILURE  + ":" + ex.getMessage());
        }
    }
}
