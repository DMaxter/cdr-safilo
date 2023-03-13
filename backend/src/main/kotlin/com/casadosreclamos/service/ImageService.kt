package com.casadosreclamos.service

import com.casadosreclamos.exception.InvalidIdException
import com.casadosreclamos.exception.OperationNotPerformedException
import com.casadosreclamos.model.Client
import com.casadosreclamos.model.request.Image
import com.casadosreclamos.repo.ImageRepository
import io.quarkus.hibernate.reactive.panache.Panache
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional
import io.smallrye.common.annotation.Blocking
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.jboss.logging.Logger
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class ImageService {
    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var imageRepository: ImageRepository

    @Inject
    lateinit var clientService: ClientService

    @Inject
    @ConfigProperty(name = "image.base")
    lateinit var imageBase: String

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

    fun addClientImage(client: Client, path: String): Uni<Image> {
        val image = Image()
        image.client = client
        image.link = "$imageBase/$path"
        client.images.add(image)

        return imageRepository.persist(image)
    }

    @Throws(InvalidIdException::class, OperationNotPerformedException::class)
    fun addImages(clientId: Long, images: Set<String>): Uni<List<Image>> {
        if (clientId <= 0) {
            logger.error("Client ID is invalid")

            throw InvalidIdException("client")
        } else if (images.isEmpty()) {
            logger.error("No images were provided")

            throw OperationNotPerformedException()
        }

        return Panache.withTransaction {
            clientService.get(clientId).onItem().transformToUni { client ->
                if (client == null) {
                    logger.error("Client with ID $clientId is not registered")

                    throw InvalidIdException("client")
                }

                val unis: MutableList<Uni<Image>> = mutableListOf()

                for (path in images) {
                    unis.add(addClientImage(client, path))
                }

                Uni.join().all(unis).andFailFast().onItem().invoke { -> logger.info("Image(s) added") }.onFailure()
                    .invoke { e -> logger.error("Couldn't add images:", e) }
            }
        }
    }
}