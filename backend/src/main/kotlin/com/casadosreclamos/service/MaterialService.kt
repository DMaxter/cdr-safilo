package com.casadosreclamos.service

import com.casadosreclamos.exception.InvalidCostException
import com.casadosreclamos.exception.InvalidNameException
import com.casadosreclamos.model.request.Material
import com.casadosreclamos.repo.MaterialRepository
import io.quarkus.hibernate.reactive.panache.Panache
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.core.Response

@ApplicationScoped
class MaterialService {
    @Inject
    lateinit var materialRepository: MaterialRepository

    fun getAll(): Multi<Material> {
        return materialRepository.streamAll()
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

        // TODO: Check for existent material

        return Panache.withTransaction { materialRepository.persist(material) }.onItem()
            .transform { Response.ok().build() }.onFailure().recoverWithItem { _ -> Response.serverError().build() }
    }
}