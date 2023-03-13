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
import javax.naming.OperationNotSupportedException
import javax.ws.rs.core.Response

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

    fun deleteImages(images: Set<Long>): Uni<Response> {
        if (images.isEmpty()) {
            logger.error("No images were provided")

            throw OperationNotPerformedException()
        } else if (images.stream().filter{ it <= 0}.count() > 0) {
            logger.error("At least one image has invalid ID")

            throw InvalidIdException("image")
        }

        return Panache.withTransaction {
            imageRepository.findAll(images).collect().asList().onItem().transformToUni { found ->
                if (found == null || found.size < images.size) {
                    logger.error("Invalid or repeated images detected. Fetched: ${found}")

                    throw InvalidIdException("image")
                }

                val unis: MutableList<Uni<Boolean>> = mutableListOf()

                for (image in found) {
                    if (image.client != null) {
                        unis.add(clientService.get(image.client!!.id).onItem().transformToUni { client ->
                            client.images.remove(image)

                            imageRepository.deleteById(image.id)
                        })
                    } else {
                        logger.error("Impossible to remove brand image")

                        throw OperationNotSupportedException()
                    }
                }

                Uni.join().all(unis).andFailFast().onItem().invoke { -> logger.info("Successfully removed client images") }.onFailure().invoke { e -> logger.error("Couldn't remove client images: ${e}") }
            }.onItem().transform { Response.ok().build() }
        }
    }
}