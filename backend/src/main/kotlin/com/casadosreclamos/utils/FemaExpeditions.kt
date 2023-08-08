package com.casadosreclamos.utils

import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

private const val SOAP_ACTION_PREFIX = "http://tempuri.org"

@Consumes(MediaType.TEXT_XML)
@Produces(MediaType.WILDCARD)
@RegisterRestClient(configKey = "fema-expeditions")
interface FemaExpeditions {
    @POST
    @ClientHeaderParam(name = "SOAPAction", value = arrayOf("${SOAP_ACTION_PREFIX}/Servicos"))
    fun getServices(body: String): Uni<String>

    @POST
    @ClientHeaderParam(name = "SOAPAction", value = arrayOf("${SOAP_ACTION_PREFIX}/Criar"))
    fun openWaybill(body: String): Uni<String>

    @POST
    @ClientHeaderParam(name = "SOAPAction", value = arrayOf("${SOAP_ACTION_PREFIX}/Etiqueta"))
    fun getWaybill(body: String): Uni<String>

    @POST
    @ClientHeaderParam(name = "SOAPAction", value = arrayOf("${SOAP_ACTION_PREFIX}/Anular"))
    fun cancelWaybill(body: String): Uni<String>

    @POST
    @ClientHeaderParam(name = "SOAPAction", value = arrayOf("${SOAP_ACTION_PREFIX}/FecharDia"))
    fun closeDay(body: String): Uni<String>
}