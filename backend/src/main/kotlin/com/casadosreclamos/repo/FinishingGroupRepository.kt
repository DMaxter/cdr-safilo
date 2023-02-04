package com.casadosreclamos.repo

import com.casadosreclamos.model.request.FinishingGroup
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import io.quarkus.panache.common.Parameters
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FinishingGroupRepository : PanacheRepository<FinishingGroup> {
    override fun streamAll(): Multi<FinishingGroup> {
        return stream("SELECT DISTINCT g FROM FinishingGroup g LEFT JOIN FETCH g.finishings")
    }

    fun stream(groups: Set<Long>): Multi<FinishingGroup> {
        return stream(
            "FROM FinishingGroup g LEFT JOIN FETCH g.finishings WHERE g.id IN (:ids)",
            Parameters.with("ids", groups).map()
        )
    }

    fun exists(name: String): Uni<Boolean> {
        return count("FROM FinishingGroup g WHERE g.name = :name", Parameters.with("name", name).map()).onItem()
            .transform { it != 0L }
    }

    fun exists(name: String, id: Long): Uni<Boolean> {
        return count(
            "FROM FinishingGroup g WHERE g.name = :name AND g.id <> :id",
            Parameters.with("name", name).and("id", id).map()
        ).onItem().transform { it != 0L }
    }
}