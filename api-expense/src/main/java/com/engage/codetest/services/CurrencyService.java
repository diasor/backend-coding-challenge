package com.engage.codetest.services;

import com.engage.codetest.api.CurrencyData;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Diana Sormani
 * Created: February 04, 2018
 * Last Updated: February 05, 2018
 * Description: The CurrencyService class contains all the information related to the entity
 *              "Expense": both the obtained by the front end application as well
 *              as the information calculated locally.
 */
public final class CurrencyService {
    private static Client client;            // A Jersey client to connect to external API to get exchange rate.
    private static String apiURL;            // The URL to access exchange rate API.
    private static String apiKey;            // The key to access exchange rate API.
    private static final String CONVERT_SERVICE_FAILURE = "The convertion rest service failed";

    private CurrencyService(){}

    public static void setCurrencyData(Client curClient, String curApiURL, String curApiKey){
        client = curClient;
        apiURL = curApiURL;
        apiKey = curApiKey;
    }

    public static BigDecimal convert(BigDecimal amountToConvert, String currSource) {
        //Check that amount to convert is present.
        if (amountToConvert.equals(BigDecimal.ZERO)) {
            return amountToConvert;
        }

        CurrencyData currencyData = client
            .target(apiURL)
            .queryParam("access_key", apiKey)
            // doesn't work with free plan .queryParam("source", currSource)
            .queryParam("currencies", "GBP,EUR")
            .request(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .get(CurrencyData.class);

        if (currencyData.getQuotes().size() > 0){
            System.out.println(currencyData.getQuotes().toString());
        }
        // Get rates to convert o US dollars (that is the only conversion available for the free api
        BigDecimal fromRate = currencyData.getQuotes().get("USDEUR");
        BigDecimal toRate = currencyData.getQuotes().get("USDGBP");

        BigDecimal convertToUS = amountToConvert.divide(fromRate,  3, RoundingMode.CEILING);
        BigDecimal convertedAmount = convertToUS.multiply(toRate);
        return convertedAmount;
        }


    public static void convertCheck() {
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
            throw new SetUpException("CONVERT", CONVERT_SERVICE_FAILURE  + ":" + ex.getMessage());
        }
    }
}
