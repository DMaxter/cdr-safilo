package com.casadosreclamos.controller

import com.casadosreclamos.dto.NewRequestDto
import com.casadosreclamos.dto.RequestDto
import com.casadosreclamos.model.*
import com.casadosreclamos.service.RequestService
import io.quarkus.security.Authenticated
import io.quarkus.security.identity.CurrentIdentityAssociation
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import org.jboss.logging.Logger
import javax.annotation.security.RolesAllowed
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.Response

@Path("/request")
@ApplicationScoped
@Tag(description = "Manage requests made by commercials")
class RequestController {
    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var identity: CurrentIdentityAssociation

    @Inject
    lateinit var requestService: RequestService

    @POST
    @RolesAllowed(COMMERCIAL_ROLE, MANAGER_ROLE, ADMIN_ROLE)
    @Operation(summary = "Issue a new request to CDR")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Successful request made"),
        APIResponse(responseCode = "401", description = "User is not logged in"),
        APIResponse(responseCode = "403", description = "User doesn't have authorization to issue a new request")
    )
    fun addRequest(request: NewRequestDto): Uni<Response> {
        return identity.deferredIdentity.onItem().transformToUni { id ->
            logger.info("User ${id.principal.name} is creating a request for client \"${request.clientId}\"")

            return@transformToUni requestService.add(request, id.principal.name)
        }
    }

    @PUT
    @RolesAllowed(CDR_ROLE, ADMIN_ROLE)
    @APIResponses(
        APIResponse(responseCode = "200", description = "Successfully put requests to production"),
        APIResponse(responseCode = "401", description = "User is not logged in"),
        APIResponse(responseCode = "403", description = "User doesn't have authorization to issue a new request")
    )
    fun requestToProduction(requests: List<Long>): Uni<Response> {
        return identity.deferredIdentity.onItem().transformToUni { id ->
            logger.info("User ${id.principal.name} put requests $requests to production")

            return@transformToUni requestService.toProduction(requests)
        }
    }

    @GET
    @Authenticated
    @Operation(summary = "Obtain all requests made to CDR")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Request obtained"),
        APIResponse(responseCode = "401", description = "User is not logged in"),
    )
    fun getAllRequest(): Multi<RequestDto> {
        return identity.deferredIdentity.onItem().transformToMulti { id ->
            logger.debug("User ${id.principal.name} is requesting all clients")

            return@transformToMulti requestService.getAll()
        }
    }

    @PUT
    @Path("/{id}")
    @Authenticated
    @Operation(summary = "Cancel a request")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Request cancelled"),
        APIResponse(responseCode = "401", description = "User is not logged in"),
    )
    fun cancelRequest(@PathParam("id") request: Long): Uni<Response> {
        return identity.deferredIdentity.onItem().transformToUni { id ->
            logger.info("User ${id.principal.name} is cancelling request with id $request")

            return@transformToUni requestService.cancel(request, id.principal.name)
        }
    }
}