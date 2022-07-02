package com.casadosreclamos.controller

import com.casadosreclamos.dto.AuthDto
import com.casadosreclamos.model.User
import com.casadosreclamos.service.AuthService
import io.quarkus.security.Authenticated
import io.smallrye.common.annotation.Blocking
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
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
    fun register(credentials: AuthDto): Uni<Response> {
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
    fun login(credentials: AuthDto): Uni<Response> {
        return authService.login(credentials)
    }

    @DELETE
    @Authenticated
    @Path("/logout")
    fun logout(): Uni<Response> {
        return authService.logout()
    }

    @POST
    @PermitAll
    @Path("/forgot/{username}")
    fun forgot(@PathParam("username") user: String): Uni<Response> {
        return authService.forgot(user)
    }
}