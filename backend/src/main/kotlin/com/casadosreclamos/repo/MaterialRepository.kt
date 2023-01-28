package com.casadosreclamos.repo

import com.casadosreclamos.dto.MaterialDto
import com.casadosreclamos.model.request.Material
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import io.quarkus.panache.common.Parameters
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class MaterialRepository : PanacheRepository<Material> {
    fun streamAllWithFinishings(): Multi<Material> {
        return stream("SELECT DISTINCT m FROM Material m LEFT JOIN FETCH m.additionalFinishings LEFT JOIN FETCH m.mandatoryFinishings")
    }

    fun exists(name: String): Uni<Boolean> {
        return count("FROM Material m WHERE m.name = :name", Parameters.with("name", name).map()).onItem()
            .transform { it != 0L }
    }

    fun find(materials: List<MaterialDto>?): Multi<Material> {
        return if (materials.isNullOrEmpty()) {
            streamAllWithFinishings()
        } else {
            stream(
                "SELECT DISTINCT m FROM Material m LEFT JOIN FETCH m.finishings WHERE m.id IN (:ids)",
                Parameters.with("ids", materials.map { it.id }).map()
            )
        }
    }
}