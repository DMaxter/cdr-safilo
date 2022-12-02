package com.casadosreclamos.service

import com.casadosreclamos.dto.BrandDto
import com.casadosreclamos.exception.AlreadyExistsException
import com.casadosreclamos.exception.InvalidIdException
import com.casadosreclamos.exception.InvalidNameException
import com.casadosreclamos.model.request.Brand
import com.casadosreclamos.model.request.Image
import com.casadosreclamos.repo.BrandRepository
import com.casadosreclamos.repo.ImageRepository
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

    @Inject
    lateinit var imageRepository: ImageRepository

    fun getAll(): Multi<BrandDto> {
        return brandRepository.streamAllWithImages().map { BrandDto(it) }
    }

    @Throws(InvalidNameException::class)
    fun add(brandName: String): Uni<Brand> {
        if (brandName.isEmpty()) {
            throw InvalidNameException()
        }

        val brand = Brand()
        brand.name = brandName
        brand.images = mutableListOf()

        return Panache.withTransaction {
            brandRepository.exists(brandName).onItem().transformToUni { value ->
                return@transformToUni if (value) {
                    throw AlreadyExistsException("Brand")
                } else {
                    brandRepository.persist(brand)
                }
            }
        }
    }

    fun addImage(brandId: Long, imageLink: String): Uni<Image> {
        return Panache.withTransaction {
            brandRepository.findByIdWithImages(brandId).onItem().transformToUni { brand ->
                if (brand == null) {
                    throw InvalidIdException("brand")
                }

                val image = Image()
                image.link = imageLink
                image.brand = brand
                brand.images.add(image)

                imageRepository.persist(image)
            }
        }
    }

    fun update(id: Long, name: String): Uni<Brand> {
        if (id <= 0) {
            throw InvalidIdException("brand")
        } else if (name.isEmpty()) {
            throw InvalidNameException()
        }

        return Panache.withTransaction {
            brandRepository.exists(name).onItem().transformToUni { value ->
                if (value) {
                    throw AlreadyExistsException("Brand")
                }

                brandRepository.findByIdWithImages(id).onItem().transform { brand ->
                    if (brand == null) {
                        throw InvalidIdException("brand")
                    }

                    brand.name = name

                    brand
                }
            }
        }
    }

    @Throws(InvalidIdException::class)
    fun delete(id: Long): Uni<Response> {
        return Panache.withTransaction { brandRepository.deleteById(id) }.onItem()
            .transform { Response.ok().build() }.onFailure().transform { InvalidIdException("brand") }
    }

    fun deleteImage(id: Long): Uni<Response> {
        return Panache.withTransaction {
            imageRepository.findById(id).onItem().transformToUni { image ->
                Uni.join().all(brandRepository.findByIdWithImages(image.brand.id).onItem().transform { brand ->
                    brand.images.remove(image)
                }, imageRepository.deleteById(id)).andFailFast().onItem().transform {
                    Response.ok().build()
                }
            }
        }
    }
}