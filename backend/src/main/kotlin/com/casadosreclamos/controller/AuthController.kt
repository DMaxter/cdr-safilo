package com.casadosreclamos.controller

import com.casadosreclamos.dto.AuthDto
import com.casadosreclamos.dto.RegisterDto
import com.casadosreclamos.dto.UserDto
import com.casadosreclamos.model.ADMIN_ROLE
import com.casadosreclamos.model.MANAGER_ROLE
import com.casadosreclamos.service.AuthService
import io.quarkus.security.Authenticated
import io.quarkus.security.identity.CurrentIdentityAssociation
import io.quarkus.security.runtime.QuarkusSecurityIdentity
import io.smallrye.mutiny.Uni
import io.vertx.ext.web.RoutingContext
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import org.jboss.logging.Logger
import javax.annotation.security.PermitAll
import javax.annotation.security.RolesAllowed
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.Response

@Path("/auth")
@ApplicationScoped
class AuthController {
    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var identity: CurrentIdentityAssociation

    @Inject
    lateinit var authService: AuthService

    @Inject
    lateinit var context: RoutingContext

    @POST
    @RolesAllowed(MANAGER_ROLE, ADMIN_ROLE)
    @Path("/register")
    @Operation(summary = "Register a new commercial user")
    @APIResponses(APIResponse(responseCode = "200", description = "Successful user registration"))
    fun registerUser(credentials: RegisterDto): Uni<UserDto> {
        return identity.deferredIdentity.onItem().transformToUni { id ->
            logger.info("User ${id.principal.name} is registering user \"${credentials.email}\"")

            return@transformToUni authService.register(credentials).onItem().transform { UserDto(it) }
        }
    }

    @POST
    @PermitAll
    @Path("/login")
    @Operation(summary = "Authenticate a user")
    @APIResponses(APIResponse(responseCode = "200", description = "Successful login"))
    fun login(credentials: AuthDto): Uni<Response> {
        logger.info("User ${credentials.email} is trying to login from ${context.request().remoteAddress()}")

        return authService.login(credentials)
    }

    @DELETE
    @Authenticated
    @Path("/logout")
    @Operation(summary = "Delete session for current user")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Successful logout"),
        APIResponse(responseCode = "401", description = "No user session exists")
    )
    fun logout(): Uni<Response> {
        return identity.deferredIdentity.onItem().transformToUni { id ->
            logger.info("User ${id.principal.name} is logging out")

            return@transformToUni authService.logout()
        }
    }

    @POST
    @PermitAll
    @Path("/forgot/{username}")
    @Operation(summary = "Request token for password reset")
    // This method should always return 200 unless an exception occurred
    // because we don't want to expose our users
    @APIResponses(APIResponse(responseCode = "200", description = "A request has been made"))
    fun forgot(@PathParam("username") user: String): Uni<Response> {
        logger.info("User \"$user\"@${context.request().remoteAddress()} is requesting a password change through token")

        return authService.forgot(user)
    }

    @POST
    @PermitAll
    @Path("/forgot/{username}/{password}/{token}")
    @Operation(summary = "Change user password")
    @APIResponses(APIResponse(responseCode = "200", description = "Password was successfully changed"))
    fun changePassword(
        @PathParam("username") user: String, @PathParam("password") password: String, @PathParam("token") token: String
    ): Uni<Response> {
        logger.info("Changing password for user \"$user\"@${context.request().remoteAddress()} through recovery token")

        return authService.changePassword(user, password, token)
    }

    @GET
    @PermitAll
    @Path("/logged")
    @Operation(summary = "Check if user logged")
    @APIResponses(APIResponse(responseCode = "200", description = "Could check if user was logged in or not"))
    fun isLogged(): Uni<Response> {
        return identity.deferredIdentity.onItem().transform { id ->
            logger.debug("User ${id.principal.name} is checking if is logged in")

            Response.ok(id is QuarkusSecurityIdentity).build()
        }
    }
}