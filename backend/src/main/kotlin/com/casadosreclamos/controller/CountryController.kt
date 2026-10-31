package com.casadosreclamos.controller

import com.casadosreclamos.dto.CountryDto
import com.casadosreclamos.utils.COUNTRIES
import io.quarkus.security.Authenticated
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.GET
import javax.ws.rs.Path

@Path("/country")
@ApplicationScoped
class CountryController {
    @GET
    @Authenticated
    @Operation(summary = "Obtain the supported countries")
    @APIResponses(
            APIResponse(responseCode = "200", description = "Countries obtained"),
            APIResponse(responseCode = "401", description = "User is not logged in")
    )
    fun getCountries(): List<CountryDto> {
        return COUNTRIES
    }
}
