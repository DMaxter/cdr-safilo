package com.casadosreclamos.repo

import com.casadosreclamos.model.Plafond
import com.casadosreclamos.model.PlafondId
import com.casadosreclamos.model.User
import com.casadosreclamos.model.request.Brand
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase
import io.quarkus.panache.common.Parameters
import io.smallrye.mutiny.Uni
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class PlafondRepository : PanacheRepositoryBase<Plafond, PlafondId> {
    fun findById(user: User, brand: Brand): Uni<Plafond> {
        return find(
            "user = :user and brand = :brand",
            Parameters.with("user", user).and("brand", brand).map()
        ).firstResult()
    }
}