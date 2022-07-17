package com.casadosreclamos.controller

import com.casadosreclamos.dto.NewRequestDto
import com.casadosreclamos.dto.RequestDto
import com.casadosreclamos.model.*
import com.casadosreclamos.service.RequestService
import io.quarkus.security.identity.CurrentIdentityAssociation
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import javax.annotation.security.RolesAllowed
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.core.Response

@Path("/request")
@ApplicationScoped
@Tag(description = "Manage requests made by commercials")
class RequestController {
    @Inject
    lateinit var requestService: RequestService

    @Inject
    lateinit var identity: CurrentIdentityAssociation

    @POST
    @RolesAllowed(COMMERCIAL_ROLE, MANAGER_ROLE, ADMIN_ROLE)
    @Operation(summary = "Issue a new request to CDR")
    @APIResponses(APIResponse(responseCode = "200", description = "Successful request made"),
    APIResponse(responseCode = "401", description = "User is not logged in"),
    APIResponse(responseCode = "403", description = "User doesn't have authorization to issue a new request"))
    fun addRequest(request: NewRequestDto): Uni<Response> {
        return identity.deferredIdentity.onItem().transformToUni { id -> requestService.add(request, id.principal.name) }
    }

    @GET
    @RolesAllowed(CDR_ROLE, MANAGER_ROLE, ADMIN_ROLE)
    @Operation(summary = "Obtain all requests made to CDR")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Request obtained"),
        APIResponse(responseCode = "401", description = "User is not logged in"),
        APIResponse(responseCode = "403", description = "User doesn't have authorization to view all requests")
    )
    fun getAllRequest(): Multi<RequestDto> {
        return requestService.getAll()
    }
}