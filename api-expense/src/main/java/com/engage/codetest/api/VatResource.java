package com.engage.codetest.api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;

/**
 * @author Diana Sormani
 * Created: February 04, 2018
 * Last Updated: February 05, 2018
 * Description: The VatResource class implements a rest endpoint that returns the system
 *              Valued Added Tax percentage.
 *              It can only be accessed via an http GET query.
 */
@Path("/vat")
public class VatResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    // Example: https://localhost:8443/vat/
    public BigDecimal getVAT() {
        return GeneralSettings.getVAT();
    }
}
