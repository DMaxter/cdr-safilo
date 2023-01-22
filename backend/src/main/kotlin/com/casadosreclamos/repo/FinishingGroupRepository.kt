package com.casadosreclamos.repo

import com.casadosreclamos.model.request.FinishingGroup
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import io.smallrye.mutiny.Multi
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FinishingGroupRepository: PanacheRepository<FinishingGroup> {
    override fun streamAll(): Multi<FinishingGroup> {
        return stream("FROM FinishingGroup g LEFT JOIN FETCH g.finishings")
    }
}