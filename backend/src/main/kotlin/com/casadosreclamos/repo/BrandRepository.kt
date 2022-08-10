package com.casadosreclamos.repo

import com.casadosreclamos.model.request.Brand
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import io.quarkus.panache.common.Parameters
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class BrandRepository : PanacheRepository<Brand> {
    fun streamAllWithImages(): Multi<Brand> {
        return stream("FROM Brand b LEFT JOIN FETCH b.images")
    }

    fun exists(name: String): Uni<Boolean> {
        return count("FROM Brand b WHERE b.name = :name", Parameters.with("name", name).map()).onItem().transform { it != 0L }
    }

    fun findByIdWithImages(id: Long): Uni<Brand> {
        return find(
            "FROM Brand b LEFT JOIN FETCH b.images WHERE b.id = :id",
            Parameters.with("id", id).map()
        ).firstResult()
    }
}