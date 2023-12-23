package com.casadosreclamos.controller

import com.casadosreclamos.dto.FinishingDto
import com.casadosreclamos.dto.NewFinishingDto
import com.casadosreclamos.model.ADMIN_ROLE
import com.casadosreclamos.model.CDR_ROLE
import com.casadosreclamos.service.FinishingService
import io.quarkus.security.Authenticated
import io.quarkus.security.identity.CurrentIdentityAssociation
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import org.jboss.logging.Logger
import javax.annotation.security.RolesAllowed
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.core.Response

@Path("/finishing")
@ApplicationScoped
class FinishingController {
    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var identity: CurrentIdentityAssociation

    @Inject
    lateinit var finishingService: FinishingService

    @POST
    @RolesAllowed(CDR_ROLE, ADMIN_ROLE)
    @Operation(summary = "Register a finishing")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Successful operation"),
        APIResponse(responseCode = "401", description = "User is not logged in"),
        APIResponse(responseCode = "403", description = "Insufficient privileges")
    )
    fun addFinishing(finishing: NewFinishingDto): Uni<FinishingDto> {
        return identity.deferredIdentity.onItem().transformToUni { id ->
            logger.info("User ${id.principal.name} is adding a new finishing")

            return@transformToUni finishingService.add(finishing).onItem().transform { FinishingDto(it) }
        }
    }

    @PUT
    @RolesAllowed(CDR_ROLE, ADMIN_ROLE)
    @Operation(summary = "Update a finishing")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Successful operation"),
        APIResponse(responseCode = "401", description = "User is not logged in"),
        APIResponse(responseCode = "403", description = "Insufficient privileges")
    )
    fun updateFinishing(finishing: FinishingDto): Uni<FinishingDto> {
        return identity.deferredIdentity.onItem().transformToUni { id ->
            logger.info("User ${id.principal.name} is changing finishing with id ${finishing.id}")

            return@transformToUni finishingService.update(finishing).onItem().transform { FinishingDto(it) }
        }
    }

    @GET
    @Authenticated
    @Operation(summary = "Obtain all registered finishings")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Successful operation"),
        APIResponse(responseCode = "401", description = "User is not logged in"),
    )
    fun getAllFinishings(): Multi<FinishingDto> {
        return identity.deferredIdentity.onItem().transformToMulti { id ->
            logger.debug("User ${id.principal.name} is requesting all finishings")

            return@transformToMulti finishingService.getAll().onItem().transform { FinishingDto(it) }
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed(CDR_ROLE, ADMIN_ROLE)
    @Operation(summary = "Remove a finishing")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Successful operation"),
        APIResponse(responseCode = "401", description = "User is not logged in"),
        APIResponse(responseCode = "403", description = "Insufficient privileges")
    )
    fun deleteFinishing(@PathParam("id") finishingId: Long): Uni<Response> {
        return identity.deferredIdentity.onItem().transformToUni { id ->
            logger.info("User ${id.principal.name} is deleting finishing with id $finishingId")

            return@transformToUni finishingService.delete(finishingId)
        }
    }

    @PUT
    @Path("/obsolete/{id}")
    @RolesAllowed(CDR_ROLE, ADMIN_ROLE)
    @Operation(summary = "Make a finishing obsolete")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Successful operation"),
        APIResponse(responseCode = "401", description = "User is not logged in"),
        APIResponse(responseCode = "403", description = "Insufficient privileges")
    )
    fun makeObsolete(@PathParam("id") finishingId: Long): Uni<Response> {
        return identity.deferredIdentity.onItem().transformToUni { id -> 
            logger.info("User ${id.principal.name} is making finishing with id $finishingId obsolete")

            return@transformToUni finishingService.makeObsolete(finishingId).onItem().transform { _ -> Response.ok().build() }
        }
    }
}
