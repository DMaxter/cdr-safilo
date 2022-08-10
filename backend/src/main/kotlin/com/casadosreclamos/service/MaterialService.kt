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
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.core.Response

@ApplicationScoped
class MaterialService {
    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var materialRepository: MaterialRepository

    fun getAll(): Multi<MaterialDto> {
        return materialRepository.streamAll().map { MaterialDto(it) }
    }

    @Throws(InvalidNameException::class, InvalidCostException::class)
    fun add(name: String, cost: Double): Uni<Response> {
        if (name.isEmpty()) {
            throw InvalidNameException()
        } else if (cost <= 0.0) {
            throw InvalidCostException()
        }

        val material = Material()
        material.cost = cost
        material.name = name

        return Panache.withTransaction {
            materialRepository.exists(name).onItem().transformToUni { value ->
                return@transformToUni if (value) {
                    throw AlreadyExistsException("Material")
                } else {
                    materialRepository.persist(material)
                }
            }
        }.onItem().transform { Response.ok().build() }
    }

    @Throws(InvalidNameException::class, InvalidCostException::class)
    fun update(id: Long, name: String, cost: Double): Uni<Response> {
        if (name.isEmpty()) {
            throw InvalidNameException()
        } else if (cost <= 0.0) {
            throw InvalidCostException()
        }

        return Panache.withTransaction {
            materialRepository.findById(id).onItem().transform { material ->
                material.name = name
                material.cost = cost

                Response.ok().build()
            }
        }
    }

    @Throws(InvalidIdException::class)
    fun delete(id: Long): Uni<Response> {
        return Panache.withTransaction { materialRepository.deleteById(id) }.onItem()
            .transform { Response.ok().build() }.onFailure().transform { InvalidIdException("material") }
    }
}