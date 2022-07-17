package com.casadosreclamos.controller

import com.casadosreclamos.service.UserService
import io.quarkus.security.Authenticated
import io.quarkus.security.identity.CurrentIdentityAssociation
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.core.Response

@Path("/user")
@ApplicationScoped
class UserController {
    @Inject
    lateinit var identity: CurrentIdentityAssociation

    @Inject
    lateinit var userService: UserService

    @GET
    @Authenticated
    @Operation(summary = "Obtain logged user information")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Information obtained"),
        APIResponse(responseCode = "401", description = "No user session exists")
    )
    fun getUserInfo(): Uni<Response> {
        return identity.deferredIdentity.onItem().transformToUni { id -> userService.getInfo(id.principal.name) }
    }
}