package com.casadosreclamos.service

import com.casadosreclamos.dto.BrandDto
import com.casadosreclamos.exception.InvalidNameException
import com.casadosreclamos.model.request.Brand
import com.casadosreclamos.repo.BrandRepository
import io.quarkus.hibernate.reactive.panache.Panache
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import org.jboss.logging.Logger
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.core.Response

@ApplicationScoped
class BrandService {
    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var brandRepository: BrandRepository

    fun getAll(): Multi<Brand> {
        return brandRepository.streamAll()
    }

    @Throws(InvalidNameException::class)
    fun add(brandName: String): Uni<Response> {
        logger.info("Adding brand $brandName")

        if (brandName.isEmpty()) {
            throw InvalidNameException()
        }

        val brand = Brand()
        brand.name = brandName
        brand.images = mutableListOf()

        // TODO: Check for existent brand

        return Panache.withTransaction { brandRepository.persist(brand) }.onItem().transform { Response.ok().build() }
            .onFailure().recoverWithItem { _ -> Response.serverError().build() }
    }

    fun addImages(brandDto: BrandDto): Uni<Response> {
        // TODO
        return Uni.createFrom().nothing()
    }
}