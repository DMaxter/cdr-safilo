package com.casadosreclamos.config

import com.casadosreclamos.exception.*
import io.smallrye.mutiny.Uni
import org.jboss.resteasy.reactive.ResponseStatus
import org.jboss.resteasy.reactive.server.ServerExceptionMapper
import org.slf4j.LoggerFactory
import javax.ws.rs.core.Response

private val LOGGER = LoggerFactory.getLogger(ExceptionMapper::class.java)

data class ExceptionError(val msg: String)

class ExceptionMapper {
    @ServerExceptionMapper
    fun map(e: Exception): Uni<Response> {
        LOGGER.error("Received general exception $e")
        e.printStackTrace()

        return Uni.createFrom().item(Response.serverError().build())
    }

    @ServerExceptionMapper
    fun map(e: CDRException): Uni<Response> {
        LOGGER.error(e.msg)
        return Uni.createFrom()
            .item(Response.status(Response.Status.BAD_REQUEST).entity(ExceptionError(e.msg)).build())
    }
}