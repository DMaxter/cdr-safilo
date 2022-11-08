package com.casadosreclamos.controller

import com.casadosreclamos.dto.ClientDto
import com.casadosreclamos.model.ADMIN_ROLE
import com.casadosreclamos.model.MANAGER_ROLE
import com.casadosreclamos.service.ClientService
import io.quarkus.security.Authenticated
import io.quarkus.security.identity.CurrentIdentityAssociation
import io.smallrye.common.annotation.Blocking
import io.smallrye.mutiny.Multi
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
            logger.info("User ${id.principal.name} is requesting all clients")

            return@transformToMulti clientService.getAll()
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
    fun registerClient(client: ClientDto): Uni<Response> {
        return identity.deferredIdentity.onItem().transformToUni { id ->
            logger.info("User ${id.principal.name} is registering client with name \"${client.name}\"")

            return@transformToUni clientService.register(client).onItem().transform {
                Response.ok(ClientDto(it)).build()
            }
        }
    }

    @POST
    @Path("/import")
    @Blocking
    @Operation(summary = "Import clients from XLSX file")
    @RolesAllowed(MANAGER_ROLE, ADMIN_ROLE)
    @APIResponses(
        APIResponse(responseCode = "200", description = "Successful operation"),
        APIResponse(responseCode = "401", description = "User is not logged in"),
        APIResponse(responseCode = "403", description = "Insufficient privileges")
    )
    fun importClients(/*@RestForm file: FileUpload*/): Uni<Response> {
        return identity.deferredIdentity.onItem().transformToUni { id ->
            logger.info("User ${id.principal.name} is importing clients from Excel file")

            //return@transformToUni clientService.importClients(file.uploadedFile().toFile()).onItem().transform { _ ->
            return@transformToUni clientService.importClients(File("/home/daniel/test.csv")).collect().asList().onItem()
                .transform {
                    return@transform Response.ok().build()
                }
        }
    }
}