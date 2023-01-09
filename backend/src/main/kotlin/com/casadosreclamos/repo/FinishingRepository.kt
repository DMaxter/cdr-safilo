package com.casadosreclamos.repo

import com.casadosreclamos.model.request.Finishing
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import io.quarkus.panache.common.Parameters
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FinishingRepository : PanacheRepository<Finishing> {
    fun streamNonExclusive(): Multi<Finishing> {
        return stream("SELECT DISTINCT f FROM Finishing f LEFT JOIN FETCH f.exclusiveMaterials WHERE f.exclusiveMaterials IS EMPTY")
    }

    fun exists(name: String): Uni<Boolean> {
        return count("FROM Finishing f WHERE f.name = :name", Parameters.with("name", name).map()).onItem()
            .transform { it != 0L }
    }
}