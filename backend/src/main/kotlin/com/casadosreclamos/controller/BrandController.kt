package com.casadosreclamos.controller

import com.casadosreclamos.model.ADMIN_ROLE
import com.casadosreclamos.model.MANAGER_ROLE
import com.casadosreclamos.model.request.Brand
import com.casadosreclamos.service.BrandService
import io.quarkus.security.Authenticated
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import javax.annotation.security.RolesAllowed
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.core.Response

@Path("/brand")
@ApplicationScoped
class BrandController {
    @Inject
    lateinit var brandService: BrandService

    @POST
    @Path("/{brand}")
    @RolesAllowed(MANAGER_ROLE, ADMIN_ROLE)
    @Operation(summary = "Register a brand")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Successful brand registration"),
        APIResponse(responseCode = "401", description = "User is not logged in"),
        APIResponse(responseCode = "403", description = "User doesn't have authorization to register a brand")
    )
    fun addBrand(@PathParam("brand") brand: String): Uni<Response> {
        return brandService.add(brand)
    }

    @GET
    @Authenticated
    @Operation(summary = "Obtain all registered brands")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Brands obtained"),
        APIResponse(responseCode = "401", description = "User is not logged in")
    )
    fun getAllBrands(): Multi<Brand> {
        return brandService.getAll()
    }
}