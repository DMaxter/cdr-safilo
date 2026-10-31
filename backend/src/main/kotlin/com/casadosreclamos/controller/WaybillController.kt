package com.casadosreclamos.controller

import com.casadosreclamos.dto.fema.LabelFormat
import com.casadosreclamos.dto.fema.PackageType
import com.casadosreclamos.dto.fema.Service
import com.casadosreclamos.dto.fema.WaybillDto
import com.casadosreclamos.model.ADMIN_ROLE
import com.casadosreclamos.model.CDR_ROLE
import com.casadosreclamos.service.WaybillService
import io.quarkus.security.identity.CurrentIdentityAssociation
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import org.jboss.logging.Logger
import java.io.File
import javax.annotation.security.RolesAllowed
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/waybill")
@ApplicationScoped
@RolesAllowed(CDR_ROLE, ADMIN_ROLE)
class WaybillController {
    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var identity: CurrentIdentityAssociation

    @Inject
    lateinit var waybillService: WaybillService

    @GET
    @Path("/services/{requestId}")
    @Operation(summary = "Obtain available shipping services for request")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Successful operation"),
        APIResponse(responseCode = "401", description = "User is not logged in"),
        APIResponse(responseCode = "403", description = "Insufficient privileges")
    )
    fun getShippingServices(@PathParam("requestId") requestId: Long): Uni<List<Service>> {
        return waybillService.getShippingServices(requestId)
    }

    @GET
    @Path("/packages")
    @Operation(summary = "Obtain package types supported")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Successful operation"),
        APIResponse(responseCode = "401", description = "User is not logged in"),
        APIResponse(responseCode = "403", description = "Insufficient privileges")
    )
    fun getPackageTypes(): Uni<List<PackageType>> {
        return waybillService.getPackageTypes()
    }

    @GET
    @Path("/formats")
    @Operation(summary = "Obtain supported label formats")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Successful operation"),
        APIResponse(responseCode = "401", description = "User is not logged in"),
        APIResponse(responseCode = "403", description = "Insufficient privileges")
    )
    fun getLabelFormats(): Uni<List<LabelFormat>> {
        return waybillService.getLabelFormats()
    }

    @POST
    @Path("/{requestId}")
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @Operation(summary = "Create waybill for given request")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Successful operation"),
        APIResponse(responseCode = "401", description = "User is not logged in"),
        APIResponse(responseCode = "403", description = "Insufficient privileges")
    )
    fun openWaybill(@PathParam("requestId") request: Long, waybillDto: WaybillDto): Uni<File> {
        return identity.deferredIdentity.onItem().transformToUni { id ->
            logger.info("User ${id.principal.name} is opening waybill for request $request")

            return@transformToUni waybillService.openWaybill(request, waybillDto)
        }
    }

    @GET
    @Path("/{requestId}/{labelFormat}")
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @Operation(summary = "Get waybill for given request")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Successful operation"),
        APIResponse(responseCode = "401", description = "User is not logged in"),
        APIResponse(responseCode = "403", description = "Insufficient privileges")
    )
    fun getWaybill(@PathParam("requestId") request: Long, @PathParam("labelFormat") labelFormat: String): Uni<File> {
        return identity.deferredIdentity.onItem().transformToUni { id ->
            logger.info("User ${id.principal.name} is obtaining waybill for request $request with format $labelFormat")

            return@transformToUni waybillService.getWaybill(request, labelFormat)
        }
    }


    @DELETE
    @Path("/{requestId}")
    @Operation(summary = "Delete waybill for given request")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Successful operation"),
        APIResponse(responseCode = "401", description = "User is not logged in"),
        APIResponse(responseCode = "403", description = "Insufficient privileges")
    )
    fun cancelWaybill(@PathParam("requestId") request: Long): Uni<Void> {
        return identity.deferredIdentity.onItem().transformToUni { id ->
            logger.info("User ${id.principal.name} is deleting waybill for request $request")

            return@transformToUni waybillService.cancelWaybill(request)
        }
    }
}