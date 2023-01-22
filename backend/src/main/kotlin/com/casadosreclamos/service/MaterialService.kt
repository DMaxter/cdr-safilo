package com.casadosreclamos.service

import com.casadosreclamos.dto.MaterialDto
import com.casadosreclamos.exception.AlreadyExistsException
import com.casadosreclamos.exception.InvalidCostException
import com.casadosreclamos.exception.InvalidIdException
import com.casadosreclamos.exception.InvalidNameException
import com.casadosreclamos.model.request.Material
import com.casadosreclamos.repo.MaterialRepository
import io.quarkus.hibernate.reactive.panache.Panache
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import org.jboss.logging.Logger
import java.util.stream.Collectors
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.core.Response

@ApplicationScoped
class MaterialService {
    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var materialRepository: MaterialRepository

    @Inject
    lateinit var finishingService: FinishingService

    fun getAll(): Multi<Material> {
        return materialRepository.streamAllWithFinishings()
    }

    fun getMaterials(materials: List<MaterialDto>?): Multi<Material> {
        return materialRepository.find(materials)
    }

    @Throws(InvalidNameException::class)
    fun add(name: String): Uni<Material> {
        if (name.isEmpty()) {
            throw InvalidNameException()
        }

        val material = Material()
        material.name = name

        return Panache.withTransaction {
            materialRepository.exists(name).onItem().transformToUni { value ->
                return@transformToUni if (value) {
                    throw AlreadyExistsException("Material")
                } else {
                    materialRepository.persist(material)
                }
            }
        }
    }

    @Throws(InvalidNameException::class)
    fun update(id: Long, name: String): Uni<Response> {
        if (name.isEmpty()) {
            throw InvalidNameException()
        }

        return Panache.withTransaction {
            materialRepository.findById(id).onItem().transform { material ->
                material.name = name

                Response.ok().build()
            }
        }
    }

    @Throws(InvalidIdException::class)
    fun delete(id: Long): Uni<Response> {
        return Panache.withTransaction { materialRepository.deleteById(id) }.onItem()
            .transform { Response.ok().build() }.onFailure().transform { InvalidIdException("material") }
    }

    fun find(id: Long): Uni<Material> {
        return materialRepository.findById(id)
    }
}