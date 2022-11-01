package com.casadosreclamos.service

import com.casadosreclamos.exception.InvalidIdException
import com.casadosreclamos.repo.ImageRepository
import io.quarkus.hibernate.reactive.panache.Panache
import io.smallrye.mutiny.Uni
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class ImageService {
  @Inject
  lateinit var imageRepository: ImageRepository

  @Throws(InvalidIdException::class)
  fun makeObsolete(id: Long): Uni<Void> {
      return Panache.withTransaction {
        imageRepository.findById(id).onItemOrFailure().transformToUni { image, error ->
            if (error != null || image == null) {
                throw InvalidIdException("image")
            }

            image.obsolete = true

            return@transformToUni Uni.createFrom().voidItem()
        }
      }
  }
}