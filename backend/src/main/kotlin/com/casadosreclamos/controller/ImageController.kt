package com.casadosreclamos.controller

import com.casadosreclamos.model.ADMIN_ROLE
import com.casadosreclamos.model.MANAGER_ROLE
import com.casadosreclamos.service.ImageService
import io.quarkus.security.identity.CurrentIdentityAssociation
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import org.jboss.logging.Logger
import javax.annotation.security.RolesAllowed
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.core.Response

@Path("/image")
@ApplicationScoped
class ImageController {
    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var identity: CurrentIdentityAssociation

    @Inject
    lateinit var imageService: ImageService

    @PUT
    @Path("/obsolete/{image}")
    @RolesAllowed(MANAGER_ROLE, ADMIN_ROLE)
    @Operation(summary = "Make an image obsolete")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Successful operation"),
        APIResponse(responseCode = "401", description = "User is not logged in"),
        APIResponse(responseCode = "403", description = "Insufficient privileges")
    )
    fun makeObsolete(@PathParam("image") image: Long): Uni<Response> {
        return identity.deferredIdentity.onItem().transformToUni { id ->
            logger.info("User ${id.principal.name} is making image $image obsolete")

            return@transformToUni imageService.makeObsolete(image).onItem().transform { _ ->
                Response.ok().build()
            }
        }
    }
}