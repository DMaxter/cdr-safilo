package com.casadosreclamos.controller

import com.casadosreclamos.dto.ImageDto
import com.casadosreclamos.model.ADMIN_ROLE
import com.casadosreclamos.model.COMMERCIAL_ROLE
import com.casadosreclamos.model.MANAGER_ROLE
import com.casadosreclamos.service.ImageService
import io.quarkus.security.identity.CurrentIdentityAssociation
import io.smallrye.common.annotation.Blocking
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import org.jboss.logging.Logger
import org.jboss.resteasy.reactive.PartType
import org.jboss.resteasy.reactive.RestForm
import org.jboss.resteasy.reactive.multipart.FileUpload
import java.io.File
import java.util.stream.Collectors
import javax.annotation.security.RolesAllowed
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import kotlin.io.path.name

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

    @POST
    @Path("/client/{clientId}")
    @RolesAllowed(COMMERCIAL_ROLE, ADMIN_ROLE)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Operation(summary = "Upload a client image")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Successful operation"),
        APIResponse(responseCode = "401", description = "User is not logged in"),
        APIResponse(responseCode = "403", description = "Insufficient privileges")
    )
    fun uploadClientImage(
        @PartType(MediaType.APPLICATION_OCTET_STREAM) @Schema(
            type = SchemaType.STRING, format = "binary"
        ) @RestForm images: List<FileUpload>, @PathParam("clientId") client: Long
    ): Uni<List<ImageDto>> {
        return identity.deferredIdentity.onItem().transformToUni { id ->
            logger.info("User ${id.principal.name} is uploading ${images.size} images to client with ID $client")

            imageService.addImages(
                client, images.stream().map { it.uploadedFile().name }.collect(
                    Collectors.toSet()
                )
            ).onItem().transform { list ->
                logger.info("Successfully uploaded images")

                list.map { ImageDto(it) }
            }
        }
    }

    @DELETE
    @Path("/client")
    @RolesAllowed(MANAGER_ROLE, ADMIN_ROLE)
    @Operation(summary = "Delete client images")
    fun deleteClientImages(images: Set<Long>): Uni<Response> {
        return identity.deferredIdentity.onItem().transformToUni { id ->
            logger.info("User ${id.principal.name} is deleting ${images.size} images")

            imageService.deleteImages(images)
        }
    }
}