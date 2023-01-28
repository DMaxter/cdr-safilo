package com.casadosreclamos.controller

import com.casadosreclamos.dto.FinishingDto
import com.casadosreclamos.dto.FinishingGroupDto
import com.casadosreclamos.model.ADMIN_ROLE
import com.casadosreclamos.model.CDR_ROLE
import com.casadosreclamos.service.FinishingService
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

@Path("/finishing/group")
@ApplicationScoped
class FinishingGroupController {
    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var identity: CurrentIdentityAssociation

    @Inject
    lateinit var finishingService: FinishingService

    @POST
    @Path("/{name}")
    @RolesAllowed(CDR_ROLE, ADMIN_ROLE)
    @Operation(summary = "Register a finishing group")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Successful operation"),
        APIResponse(responseCode = "401", description = "User is not logged in"),
        APIResponse(responseCode = "403", description = "Insufficient privileges")
    )
    fun createGroup(@PathParam("name") name: String, finishings: Set<FinishingDto>): Uni<FinishingGroupDto> {
        return identity.deferredIdentity.onItem().transformToUni { id ->
            logger.info("User ${id.principal.name} is creating a group of finishings with name $name")

            return@transformToUni finishingService.createGroup(name, finishings).onItem()
                .transform { FinishingGroupDto(it) }
        }
    }

    @GET
    @Authenticated
    @Operation(summary = "Get all finishing groups")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Successful operation"),
        APIResponse(responseCode = "401", description = "User is not logged in"),
    )
    fun getAllGroups(): Multi<FinishingGroupDto> {
        return identity.deferredIdentity.onItem().transformToMulti { id ->
            logger.debug("User ${id.principal.name} is requesting all finishing groups")

            return@transformToMulti finishingService.getGroups().onItem().transform { FinishingGroupDto(it) }
        }
    }

    @PUT
    @RolesAllowed(CDR_ROLE, ADMIN_ROLE)
    @Operation(summary = "Edit a finishing group")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Successful operation"),
        APIResponse(responseCode = "401", description = "User is not logged in"),
        APIResponse(responseCode = "403", description = "Insufficient privileges")
    )
    fun editGroup(group: FinishingGroupDto): Uni<FinishingGroupDto> {
        return identity.deferredIdentity.onItem().transformToUni { id ->
            logger.info("User ${id.principal.name} is editing a finishing group")

            return@transformToUni finishingService.editGroup(group).onItem().transform { FinishingGroupDto(it) }
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed(CDR_ROLE, ADMIN_ROLE)
    @Operation(summary = "Delete a finishing group")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Successful operation"),
        APIResponse(responseCode = "401", description = "User is not logged in"),
        APIResponse(responseCode = "403", description = "Insufficient privileges")
    )
    fun deleteGroup(@PathParam("id") groupId: Long): Uni<Response> {
        return identity.deferredIdentity.onItem().transformToUni { id ->
            logger.info("User ${id.principal.name} is deleting finishing group with id $groupId")

            return@transformToUni finishingService.deleteGroup(groupId)
        }
    }
}