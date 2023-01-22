package com.casadosreclamos.repo

import com.casadosreclamos.model.request.Price
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import io.quarkus.panache.common.Parameters
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class PriceRepository : PanacheRepository<Price> {
    fun stream(material: Long): Multi<Price> {
        return stream(
            "FROM Price p LEFT JOIN FETCH p.finishings WHERE p.material.id = :material",
            Parameters.with("material", material).map()
        )
    }
}