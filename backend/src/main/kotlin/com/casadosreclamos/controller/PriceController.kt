package com.casadosreclamos.controller

import com.casadosreclamos.dto.PriceDto
import com.casadosreclamos.model.ADMIN_ROLE
import com.casadosreclamos.model.CDR_ROLE
import com.casadosreclamos.service.PriceService
import io.quarkus.security.identity.CurrentIdentityAssociation
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import org.jboss.logging.Logger
import javax.annotation.security.RolesAllowed
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.DELETE
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam

@Path("/price")
@ApplicationScoped
class PriceController {
    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var identity: CurrentIdentityAssociation

    @Inject
    lateinit var priceService: PriceService

    @POST
    @RolesAllowed(CDR_ROLE, ADMIN_ROLE)
    @Operation(summary = "Register a price for given measurements")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Successful operation"),
        APIResponse(responseCode = "401", description = "User is not logged in"),
        APIResponse(responseCode = "403", description = "Insufficient privileges")
    )
    fun addPrice(price: PriceDto): Uni<PriceDto> {
        return identity.deferredIdentity.onItem().transformToUni { id ->
            logger.info("User ${id.principal.name} is adding a new price")

            return@transformToUni priceService.add(price).onItem().transform { PriceDto(it) }
        }
    }

    @PUT
    @RolesAllowed(CDR_ROLE, ADMIN_ROLE)
    @Operation(summary = "Edit price for given measurement")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Successful operation"),
        APIResponse(responseCode = "401", description = "User is not logged in"),
        APIResponse(responseCode = "403", description = "Insufficient privileges")
    )
    fun editPrice(price: PriceDto): Uni<PriceDto> {
        return identity.deferredIdentity.onItem().transformToUni { id ->
            logger.info("User ${id.principal.name} is editing a price")

            return@transformToUni priceService.edit(price).onItem().transform { PriceDto(it) }
        }
    }

    @DELETE
    @RolesAllowed(CDR_ROLE, ADMIN_ROLE)
    @Operation(summary = "Delete price for given measurement")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Successful operation"),
        APIResponse(responseCode = "401", description = "User is not logged in"),
        APIResponse(responseCode = "403", description = "Insufficient privileges")
    )
    fun deletePrice(price: PriceDto): Uni<Void> {
        return identity.deferredIdentity.onItem().transformToUni { id ->
            logger.info("User ${id.principal.name} is deleting a price")

            return@transformToUni priceService.delete(price)
        }
    }
}