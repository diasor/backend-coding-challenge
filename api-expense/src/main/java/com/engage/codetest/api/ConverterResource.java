package com.engage.codetest.api;

import com.engage.codetest.services.CurrencyService;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;

@Path("/converter")
public class ConverterResource {

    @GET
    @Path("/{amount}")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    @JsonProperty("quotes")
    // Example: https://localhost:8443/converter/800?EUR
    public BigDecimal convert(@PathParam("amount") BigDecimal amountToConvert, @QueryParam("source") String currSource) {
        return CurrencyService.convert(amountToConvert, currSource);
    }
}
