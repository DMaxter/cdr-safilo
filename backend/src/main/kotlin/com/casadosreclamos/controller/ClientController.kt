package com.casadosreclamos.controller

import com.casadosreclamos.dto.ClientDto
import com.casadosreclamos.model.ADMIN_ROLE
import com.casadosreclamos.model.CDR_ROLE
import com.casadosreclamos.model.COMMERCIAL_ROLE
import com.casadosreclamos.model.MANAGER_ROLE
import com.casadosreclamos.service.ClientService
import io.quarkus.security.Authenticated
import io.quarkus.security.identity.CurrentIdentityAssociation
import io.smallrye.common.annotation.Blocking
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import org.jboss.logging.Logger
import org.jboss.resteasy.reactive.PartType
import org.jboss.resteasy.reactive.RestForm
import org.jboss.resteasy.reactive.multipart.FileUpload
import javax.annotation.security.RolesAllowed
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/client")
@ApplicationScoped
class ClientController {
    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var identity: CurrentIdentityAssociation

    @Inject
    lateinit var clientService: ClientService

    @GET
    @Authenticated
    @Operation(summary = "Obtain all registered clients")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Clients obtained"),
        APIResponse(responseCode = "401", description = "User is not logged in")
    )
    fun getAllClients(): Multi<ClientDto> {
        return identity.deferredIdentity.onItem().transformToMulti { id ->
            logger.debug("User ${id.principal.name} is requesting all clients")

            return@transformToMulti clientService.getAll().onItem().transform {
                ClientDto(it)
            }
        }
    }

    @POST
    @Operation(summary = "Register a client")
    @RolesAllowed(MANAGER_ROLE, ADMIN_ROLE)
    @APIResponses(
        APIResponse(responseCode = "200", description = "Successful client registration"),
        APIResponse(responseCode = "401", description = "User is not logged in"),
        APIResponse(responseCode = "403", description = "User doesn't have authorization to register a client")
    )
    fun addClient(client: ClientDto): Uni<ClientDto> {
        return identity.deferredIdentity.onItem().transformToUni { id ->
            logger.info("User ${id.principal.name} is registering client with name \"${client.name}\"")

            return@transformToUni clientService.add(client).onItem().transform {
                ClientDto(it)
            }
        }
    }

    @PUT
    @Operation(summary = "Edit a client")
    @RolesAllowed(MANAGER_ROLE, ADMIN_ROLE)
    @APIResponses(
        APIResponse(responseCode = "200", description = "Successful client registration"),
        APIResponse(responseCode = "401", description = "User is not logged in"),
        APIResponse(responseCode = "403", description = "User doesn't have authorization to edit a client")
    )
    fun editClient(client: ClientDto): Uni<ClientDto> {
        return identity.deferredIdentity.onItem().transformToUni { id ->
            logger.info("User ${id.principal.name} is editing client with name \"${client.name}\"")

            return@transformToUni clientService.update(client).onItem().transform {
                ClientDto(it)
            }
        }
    }

    // Different endpoint as this is managed by CDR ONLY
    @PUT
    @Path("/{id}")
    @Operation(summary = "Add a note")
    @RolesAllowed(CDR_ROLE, ADMIN_ROLE)
    @APIResponses(
        APIResponse(responseCode = "200", description = "Successful client registration"),
        APIResponse(responseCode = "401", description = "User is not logged in"),
        APIResponse(responseCode = "403", description = "User doesn't have authorization to edit a client")
    )
    fun editClientNote(@PathParam("id") clientId: Long, note: String): Uni<ClientDto> {
        return identity.deferredIdentity.onItem().transformToUni { id ->
            logger.info("User ${id.principal.name} is editing note in client with ID $clientId")

            return@transformToUni clientService.editNote(clientId, note).onItem().transform {
                ClientDto(it)
            }
        }
    }

    @POST
    @Blocking
    @Path("/import")
    @RolesAllowed(MANAGER_ROLE, ADMIN_ROLE)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Operation(summary = "Import clients from XLSX file")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Successful operation"),
        APIResponse(responseCode = "401", description = "User is not logged in"),
        APIResponse(responseCode = "403", description = "Insufficient privileges")
    )
    fun importClients(
        @PartType(MediaType.APPLICATION_OCTET_STREAM) @Schema(
            type = SchemaType.STRING, format = "binary"
        ) @RestForm file: FileUpload
    ): Uni<Response> {
        return identity.deferredIdentity.onItem().transform { id ->
            logger.info("User ${id.principal.name} is importing clients from Excel file")

            clientService.importClients(file.uploadedFile().toFile()).onCompletion()
                .invoke { -> logger.info("Successfully uploaded all clients") }.collect().asList().await()
                .indefinitely()

            // Delete uploaded file
            file.uploadedFile().toFile().delete()

            logger.info("Temporary file with clients deleted successfully")

            Response.ok().build()
        }
    }

    @GET
    @Path("/banner/{banner}")
    @Operation(summary = "Obtain clients of a banner")
    @RolesAllowed(COMMERCIAL_ROLE, MANAGER_ROLE, ADMIN_ROLE)
    @APIResponses(
        APIResponse(responseCode = "200", description = "Successful operation"),
        APIResponse(responseCode = "401", description = "User is not logged in"),
        APIResponse(responseCode = "403", description = "Insufficient privileges")
    )
    fun getBannerClients(@PathParam("banner") banner: String): Multi<ClientDto> {
        return identity.deferredIdentity.onItem().transformToMulti { id ->
            logger.info("User ${id.principal.name} is getting clients of banner $banner")

            return@transformToMulti clientService.getByBanner(banner).onItem().transform {
                ClientDto(it)
            }
        }
    }
}