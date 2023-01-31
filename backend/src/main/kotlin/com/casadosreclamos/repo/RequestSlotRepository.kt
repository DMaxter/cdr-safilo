package com.casadosreclamos.repo

import com.casadosreclamos.model.request.RequestSlot
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import io.quarkus.panache.common.Parameters
import io.smallrye.mutiny.Uni
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class RequestSlotRepository : PanacheRepository<RequestSlot> {
    override fun findById(id: Long?): Uni<RequestSlot> {
        return find(
            "FROM RequestSlot s LEFT JOIN FETCH s.material.mandatoryFinishings WHERE s.id = :id",
            Parameters.with("id", id).map()
        ).firstResult()
    }
}