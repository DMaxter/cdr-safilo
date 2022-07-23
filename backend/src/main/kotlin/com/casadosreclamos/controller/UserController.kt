package com.casadosreclamos.controller

import com.casadosreclamos.service.UserService
import io.quarkus.security.Authenticated
import io.quarkus.security.identity.CurrentIdentityAssociation
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import org.jboss.logging.Logger
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.core.Response

@Path("/user")
@ApplicationScoped
class UserController {
    @Inject
    lateinit var logger: Logger

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
        return identity.deferredIdentity.onItem().transformToUni { id ->
            logger.info("User ${id.principal.name} is requesting their personal information")

            userService.getInfo(id.principal.name)
        }
    }

    @PUT
    @Path("/password/{old}/{new}")
    @Authenticated
    @Operation(summary = "Change user password")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Password updated"),
        APIResponse(responseCode = "401", description = "No user session exists")
    )
    fun changePassword(@PathParam("old") old: String, @PathParam("new") new: String): Uni<Response> {
        return identity.deferredIdentity.onItem().transformToUni { id ->
            logger.info("User ${id.principal.name} is changing their password")

            userService.changePassword(id.principal.name, old, new)
        }
    }
}