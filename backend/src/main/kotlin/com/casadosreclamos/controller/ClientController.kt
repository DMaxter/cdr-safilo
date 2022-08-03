package com.casadosreclamos.controller

import com.casadosreclamos.dto.AddressDto
import com.casadosreclamos.dto.ClientDto
import com.casadosreclamos.model.ADMIN_ROLE
import com.casadosreclamos.model.Client
import com.casadosreclamos.model.MANAGER_ROLE
import com.casadosreclamos.service.ClientService
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
        return identity.deferredIdentity.onItem().transformToMulti {id ->
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

            return@transformToUni clientService.register(client)
        }
    }

    @PUT
    @Path("/address/{id}")
    @Operation(summary = "Add address to client")
    @RolesAllowed(MANAGER_ROLE, ADMIN_ROLE)
    @APIResponses(
        APIResponse(responseCode = "200", description = "Successful address registration"),
        APIResponse(responseCode = "401", description = "User is not logged in"),
        APIResponse(responseCode = "403", description = "User doesn't have authorization to add a client address")
    )
    fun addClientAddress(@PathParam("id") client: Long, address: AddressDto): Uni<Response> {
        return identity.deferredIdentity.onItem().transformToUni { id ->
            logger.info("User ${id.principal.name} is adding an address to client with id $client")

            return@transformToUni clientService.addAddress(client, address)
        }
    }
}