package com.casadosreclamos.controller

import com.casadosreclamos.dto.AuthDto
import com.casadosreclamos.model.User
import com.casadosreclamos.service.AuthService
import io.quarkus.security.Authenticated
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import javax.annotation.security.PermitAll
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.Response

@Path("/auth")
@ApplicationScoped
class AuthController {
    @Inject
    lateinit var authService: AuthService

    @POST
    @PermitAll
    @Path("/register")
    fun registerUser(credentials: AuthDto): Uni<Response> {
        return authService.register(credentials)
    }

    @GET
    @PermitAll
    @Path("/all")
    fun getAllUsers(): Multi<User> {
        return authService.getAll()
    }

    @POST
    @PermitAll
    @Path("/login")
    @Operation(summary = "Authenticate a user")
    @APIResponses(value = [
        APIResponse(responseCode = "200", description = "Successful login"),
        APIResponse(responseCode = "400", description = "Invalid credentials")
    ])
    fun login(credentials: AuthDto): Uni<Response> {
        return authService.login(credentials)
    }

    @DELETE
    @Authenticated
    @Path("/logout")
    @Operation(summary = "Delete session for current user")
    @APIResponses(value = [
        APIResponse(responseCode = "200", description = "Successful logout"),
        APIResponse(responseCode = "401", description = "There is no current logged in user")
    ])
    fun logout(): Uni<Response> {
        return authService.logout()
    }

    @POST
    @PermitAll
    @Path("/forgot/{username}")
    @Operation(summary = "Request token for password reset")
    // This method should always return 200 unless an exception occurred
    // because we don't want to expose our users
    @APIResponses(value = [APIResponse(responseCode = "200", description = "A request has been made")])
    fun forgot(@PathParam("username") user: String): Uni<Response> {
        return authService.forgot(user)
    }

    @POST
    @PermitAll
    @Path("/forgot/{username}/{password}/{token}")
    @Operation(summary = "Change user password")
    @APIResponses(value = [
        APIResponse(responseCode = "200", description = "Password was successfully changed"),
        APIResponse(responseCode = "401", description = "Token did not match the one requested by user")
    ])
    fun changePassword(@PathParam("username") user: String, @PathParam("password") password: String, @PathParam("token") token: String): Uni<Response> {
        return authService.changePassword(user, password, token)
    }
}