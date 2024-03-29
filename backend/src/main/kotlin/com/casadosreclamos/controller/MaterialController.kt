package com.casadosreclamos.controller

import com.casadosreclamos.dto.MaterialDto
import com.casadosreclamos.model.ADMIN_ROLE
import com.casadosreclamos.model.CDR_ROLE
import com.casadosreclamos.model.request.Material
import com.casadosreclamos.service.MaterialService
import io.quarkus.security.Authenticated
import io.quarkus.security.identity.CurrentIdentityAssociation
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import org.jboss.logging.Logger
import javax.annotation.security.RolesAllowed
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.Response

@Path("/material")
@ApplicationScoped
class MaterialController {
    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var identity: CurrentIdentityAssociation

    @Inject
    lateinit var materialService: MaterialService

    @POST
    @RolesAllowed(CDR_ROLE, ADMIN_ROLE)
    @Operation(summary = "Register a material")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Successful material registration"),
        APIResponse(responseCode = "401", description = "User is not logged in"),
        APIResponse(responseCode = "403", description = "User doesn't have authorization to register a material")
    )
    fun addMaterial(material: MaterialDto): Uni<MaterialDto> {
        return identity.deferredIdentity.onItem().transformToUni { id ->
            logger.info("User ${id.principal.name} is adding material")

            return@transformToUni materialService.add(material).onItem().transform { MaterialDto(it) }
        }
    }

    @GET
    @Authenticated
    @Operation(summary = "Obtain all registered materials")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Materials obtained"),
        APIResponse(responseCode = "401", description = "User is not logged in")
    )
    fun getAllMaterials(): Multi<MaterialDto> {
        return identity.deferredIdentity.onItem().transformToMulti { id ->
            logger.debug("User ${id.principal.name} is requesting all materials")

            return@transformToMulti materialService.getAll().onItem().transform { MaterialDto(it) }
        }
    }

    @PUT
    @RolesAllowed(CDR_ROLE, ADMIN_ROLE)
    @Operation(summary = "Updates a material")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Successful material update"),
        APIResponse(responseCode = "401", description = "User is not logged in"),
        APIResponse(responseCode = "403", description = "User doesn't have authorization to register a material")
    )
    fun updateMaterial(material: MaterialDto): Uni<MaterialDto> {
        return identity.deferredIdentity.onItem().transformToUni { id ->
            logger.info("User ${id.principal.name} is updating a material")

            return@transformToUni materialService.update(material).onItem().transform { MaterialDto(it) }
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed(CDR_ROLE, ADMIN_ROLE)
    @Operation(summary = "Remove a material")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Successful material deletion"),
        APIResponse(responseCode = "401", description = "User is not logged in"),
        APIResponse(responseCode = "403", description = "User doesn't have authorization to register a material")
    )
    fun deleteMaterial(@PathParam("id") material: Long): Uni<Response> {
        return identity.deferredIdentity.onItem().transformToUni { id ->
            logger.info("User ${id.principal.name} is deleting material with id $material")

            return@transformToUni materialService.delete(material)
        }
    }
}