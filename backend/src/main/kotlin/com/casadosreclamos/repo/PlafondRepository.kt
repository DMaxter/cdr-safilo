package com.casadosreclamos.repo

import com.casadosreclamos.model.Plafond
import com.casadosreclamos.model.PlafondId
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase
import io.quarkus.panache.common.Parameters
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class PlafondRepository : PanacheRepositoryBase<Plafond, PlafondId> {
    fun findById(user: String, brand: Long): Uni<Plafond> {
        return find(
            "user = :user and brand = :brand",
            Parameters.with("user", user).and("brand", brand).map()
        ).firstResult()
    }

    fun findByUser(user: String): Multi<Plafond> {
        return stream("user = :user", Parameters.with("user", user).map())
    }
}