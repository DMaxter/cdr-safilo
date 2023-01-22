package com.casadosreclamos.repo

import com.casadosreclamos.model.request.Finishing
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import io.quarkus.panache.common.Parameters
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FinishingRepository : PanacheRepository<Finishing> {
    fun stream(finishings: Set<Long>): Multi<Finishing> {
        return stream("FROM Finishing f WHERE f.id IN (:ids)", Parameters.with("ids", finishings).map())
    }

    fun exists(name: String): Uni<Boolean> {
        return count("FROM Finishing f WHERE f.name = :name", Parameters.with("name", name).map()).onItem()
            .transform { it != 0L }
    }
}