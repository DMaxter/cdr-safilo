package com.casadosreclamos.controller

import com.casadosreclamos.dto.BrandDto
import com.casadosreclamos.dto.ImageDto
import com.casadosreclamos.model.ADMIN_ROLE
import com.casadosreclamos.model.MANAGER_ROLE
import com.casadosreclamos.service.BrandService
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

@Path("/brand")
@ApplicationScoped
class BrandController {
    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var identity: CurrentIdentityAssociation

    @Inject
    lateinit var brandService: BrandService

    @POST
    @Path("/{brand}")
    @RolesAllowed(MANAGER_ROLE, ADMIN_ROLE)
    @Operation(summary = "Register a brand")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Successful brand registration"),
        APIResponse(responseCode = "401", description = "User is not logged in"),
        APIResponse(responseCode = "403", description = "User doesn't have authorization to register a brand")
    )
    fun addBrand(@PathParam("brand") brand: String): Uni<BrandDto> {
        return identity.deferredIdentity.onItem().transformToUni { id ->
            logger.info("User ${id.principal.name} is adding brand \"$brand\"")

            return@transformToUni brandService.add(brand).onItem().transform { BrandDto(it) }
        }
    }

    @PUT
    @Path("/{id}/{name}")
    @RolesAllowed(MANAGER_ROLE, ADMIN_ROLE)
    @Operation(summary = "Update a brand")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Successful operation"),
        APIResponse(responseCode = "401", description = "User is not logged in"),
        APIResponse(responseCode = "403", description = "Insufficient privileges")
    )
    fun updateBrand(@PathParam("id") brandId: Long, @PathParam("name") name: String) : Uni<BrandDto> {
        return identity.deferredIdentity.onItem().transformToUni { id ->
            logger.info("User ${id.principal.name} is changing brand with id ${brandId} to ${name}")

            brandService.update(brandId, name).onItem().transform { BrandDto(it) }
        }
    }

    @GET
    @Authenticated
    @Operation(summary = "Obtain all registered brands")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Brands obtained"),
        APIResponse(responseCode = "401", description = "User is not logged in")
    )
    fun getAllBrands(): Multi<BrandDto> {
        return identity.deferredIdentity.onItem().transformToMulti { id ->
            logger.debug("User ${id.principal.name} is requesting all brands")

            return@transformToMulti brandService.getAll()
        }
    }

    @POST
    @Path("/image/{id}")
    @RolesAllowed(MANAGER_ROLE, ADMIN_ROLE)
    @Operation(summary = "Add images to a brand")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Successful image registration"),
        APIResponse(responseCode = "401", description = "User is not logged in"),
        APIResponse(responseCode = "403", description = "User doesn't have authorization to add images to a brand")
    )
    fun addImages(@PathParam("id") brand: Long, image: String): Uni<ImageDto> {
        return identity.deferredIdentity.onItem().transformToUni { id ->
            logger.info("User ${id.principal.name} is adding an image to brand with id $brand")

            return@transformToUni brandService.addImage(brand, image).onItem().transform { ImageDto(it) }
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed(MANAGER_ROLE, ADMIN_ROLE)
    @Operation(summary = "Remove a brand")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Successful brand deletion"),
        APIResponse(responseCode = "401", description = "User is not logged in"),
        APIResponse(responseCode = "403", description = "User doesn't have authorization to delete brand")
    )
    fun deleteBrand(@PathParam("id") brand: Long): Uni<Response> {
        return identity.deferredIdentity.onItem().transformToUni { id ->
            logger.info("User ${id.principal.name} is deleting brand with id $brand")

            return@transformToUni brandService.delete(brand)
        }
    }

    @DELETE
    @Path("/image/{id}")
    @RolesAllowed(MANAGER_ROLE, ADMIN_ROLE)
    @Operation(summary = "Delete image")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Successful image deletion"),
        APIResponse(responseCode = "401", description = "User is not logged in"),
        APIResponse(responseCode = "403", description = "User doesn't have authorization to delete image")
    )
    fun deleteImage(@PathParam("id") image: Long): Uni<Response> {
        return identity.deferredIdentity.onItem().transformToUni { id ->
            logger.info("User ${id.principal.name} is deleting image with id $image")

            return@transformToUni brandService.deleteImage(image)
        }
    }
}