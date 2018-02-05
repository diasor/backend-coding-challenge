package com.engage.codetest.api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;

@Path("/vat")
public class VATResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    // Example: https://localhost:8443/vat/
    public BigDecimal getVAT() {
        return GeneralSettings.getVAT();
    }
}
