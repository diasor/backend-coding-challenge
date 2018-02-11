package com.engage.codetest;

import io.swagger.annotations.*;

@SwaggerDefinition(info = @Info(
        description = "This is a set of rest apis for a code challenge for Engage",
        version = "V1.0",
        title = "Code test challenge APIs",
        contact = @Contact(
                name = "Diana Sormani",
                email = "diana.sormani@gmail.com",
                url = "https://localhost:8443/expense"
        )
),
        consumes = {"application/json", "application/xml"},
        produces = {"application/json", "application/xml"},
        schemes = {SwaggerDefinition.Scheme.HTTP, SwaggerDefinition.Scheme.HTTPS},
        tags = {
                @Tag(name = "Private", description = "Tag used to denote operations as private")
        },
        externalDocs = @ExternalDocs(value = "Currency converter", url = "http://apilayer.net/api/live?")
)
public interface CodeTestSwaggerDefinition {

}
