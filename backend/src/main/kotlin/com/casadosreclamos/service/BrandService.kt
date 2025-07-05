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
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.core.Response
import org.jboss.logging.Logger

@ApplicationScoped
class BrandService {
    @Inject lateinit var logger: Logger

    @Inject lateinit var brandRepository: BrandRepository

    @Inject lateinit var imageRepository: ImageRepository

    fun getAll(): Multi<BrandDto> {
        return brandRepository.streamAllWithImages().map { BrandDto(it) }
    }

    @Throws(InvalidNameException::class)
    fun add(brandName: String): Uni<Brand> {
        if (brandName.isEmpty()) {
            logger.error("Name is empty")

            throw InvalidNameException()
        }

        val brand = Brand()
        brand.name = brandName
        brand.images = mutableListOf()

        return Panache.withTransaction {
            brandRepository.exists(brandName).onItem().transformToUni { value ->
                return@transformToUni if (value) {
                    logger.error("A brand with name $brandName is already registered")

                    throw AlreadyExistsException("Marca")
                } else {
                    brandRepository
                            .persist(brand)
                            .onItem()
                            .invoke { _ -> logger.info("Successfully registered brand") }
                            .onFailure()
                            .invoke { e -> logger.error("Couldn't register brand: $e") }
                }
            }
        }
    }

    fun addImage(brandId: Long, imageLink: String): Uni<Image> {
        logger.info("Adding link $imageLink to brand with ID $brandId")

        return Panache.withTransaction {
            brandRepository.findByIdWithImages(brandId).onItem().transformToUni { brand ->
                if (brand == null) {
                    logger.error("Brand with ID $brandId is not registered")

                    throw InvalidIdException("marca")
                }

                val image = Image()
                image.link = imageLink
                image.brand = brand
                brand.images.add(image)

                logger.info("Added image to brand")

                imageRepository
                        .persist(image)
                        .onItem()
                        .invoke { _ -> logger.info("Successfully added image") }
                        .onFailure()
                        .invoke { e -> logger.error("Couldn't save image: $e") }
            }
        }
    }

    fun update(id: Long, name: String): Uni<Brand> {
        if (id <= 0) {
            logger.error("Brand ID is invalid")

            throw InvalidIdException("marca")
        } else if (name.isEmpty()) {
            logger.error("Name is empty")

            throw InvalidNameException()
        }

        return Panache.withTransaction {
            brandRepository.exists(name, id).onItem().transformToUni { value ->
                if (value) {
                    logger.error("A brand with name $name is already registered")

                    throw AlreadyExistsException("Marca")
                }

                brandRepository.findByIdWithImages(id).onItem().transform { brand ->
                    if (brand == null) {
                        logger.error("Brand with ID $id is not registered")

                        throw InvalidIdException("marca")
                    }

                    brand.name = name

                    logger.info("Successfully change brand name")

                    brand
                }
            }
        }
    }

    @Throws(InvalidIdException::class)
    fun delete(id: Long): Uni<Response> {
        return Panache.withTransaction { brandRepository.deleteById(id) }
                .onItem()
                .transform {
                    logger.info("Successfully deleted brand")

                    Response.ok().build()
                }
                .onFailure()
                .transform { e ->
                    logger.error("Couldn't delete brand: $e")

                    InvalidIdException("marca")
                }
    }

    fun deleteImage(id: Long): Uni<Response> {
        return Panache.withTransaction {
            imageRepository.findById(id).onItem().transformToUni { image ->
                if (image == null) {
                    logger.error("Image doesn't exist")

                    throw InvalidIdException("imagem")
                }

                Uni.join()
                        .all(
                                brandRepository
                                        .findByIdWithImages(image.brand!!.id)
                                        .onItem()
                                        .transform { brand -> brand.images.remove(image) },
                                imageRepository.deleteById(id)
                        )
                        .andFailFast()
                        .onItem()
                        .transform {
                            logger.info("Successfully deleted image")

                            Response.ok().build()
                        }
                        .onFailure()
                        .invoke { e -> logger.error("Couldn't delete image: $e") }
            }
        }
    }
}
