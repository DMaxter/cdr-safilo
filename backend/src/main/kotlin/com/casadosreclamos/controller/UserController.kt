package com.casadosreclamos.controller

import com.casadosreclamos.service.UserService
import io.quarkus.security.Authenticated
import io.quarkus.security.identity.CurrentIdentityAssociation
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import java.security.Principal
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.core.Context
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
    @APIResponses(value = [
        APIResponse(responseCode = "200", description = "Obtain user information"),
        APIResponse(responseCode = "401", description = "User not allowed")
    ])
    fun getUserInfo(): Uni<Response> {
        return identity.deferredIdentity.onItem().transformToUni { id -> userService.getInfo(id.principal.name) }
    }
}