package com.engage.codetest.api;

import com.engage.codetest.ExpensesApplication;
import com.engage.codetest.services.CurrencyService;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;

/**
 * @author Diana Sormani
 * Created: February 04, 2018
 * Last Updated: February 05, 2018
 * Description: The ConverterResource class implements a rest endpoint that returns the
 *              conversion rate from EUR to GBP
 *              It can only be accessed via an http GET request.
 */
@Path("/converter")
public class ConverterResource {
    private final String CURRENCY_CONVERTER_ERROR = "CURRENCY_CONVERTER_ERROR";

    @GET
    @Path("/{amount}")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    @JsonProperty("quotes")
    // Example: https://localhost:8443/converter/800
    public BigDecimal convert(@PathParam("amount") BigDecimal amountToConvert) {
        try {
            return CurrencyService.convertEURToGBP(amountToConvert);
        }
        catch (Exception ex){
            String exceptionString = CURRENCY_CONVERTER_ERROR + ex.getCause().getLocalizedMessage();
            ExpensesApplication.expenseLogger.error(exceptionString);
        }
        return BigDecimal.ZERO;
    }
}
