package com.casadosreclamos.config

import com.casadosreclamos.exception.*
import io.smallrye.mutiny.Uni
import org.jboss.resteasy.reactive.server.ServerExceptionMapper
import org.slf4j.LoggerFactory
import javax.ws.rs.core.Response

private val LOGGER = LoggerFactory.getLogger(ExceptionMapper::class.java)

data class ExceptionError(val msg: String)

class ExceptionMapper {
    //@ServerExceptionMapper
    fun map(e: Exception): Uni<Response> {
       LOGGER.warn("Received general exception $e")

       return Uni.createFrom().item(Response.serverError().entity(e).build())
    }

    @ServerExceptionMapper
    fun map(e: InvalidCredentialsException): Uni<Response> {
        LOGGER.error("Invalid credentials")
        return Uni.createFrom().item(Response.status(Response.Status.BAD_REQUEST).entity(ExceptionError("Invalid credentials")).build())
    }
}