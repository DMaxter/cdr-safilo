package com.casadosreclamos.service

import com.casadosreclamos.exception.InvalidIdException
import com.casadosreclamos.repo.ImageRepository
import io.quarkus.hibernate.reactive.panache.Panache
import io.smallrye.mutiny.Uni
import org.jboss.logging.Logger
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class ImageService {
    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var imageRepository: ImageRepository

    @Throws(InvalidIdException::class)
    fun makeObsolete(id: Long): Uni<Void> {
        return Panache.withTransaction {
            imageRepository.findById(id).onItem().transformToUni { image ->
                if (image == null) {
                    logger.error("Image with ID $id is not registered")

                    throw InvalidIdException("image")
                }

                image.obsolete = true

                logger.info("Successfully marked image as obsolete")

                return@transformToUni Uni.createFrom().voidItem()
            }.onFailure().invoke { e -> logger.error("Couldn't mark image as obsolete: $e") }
        }
    }
}