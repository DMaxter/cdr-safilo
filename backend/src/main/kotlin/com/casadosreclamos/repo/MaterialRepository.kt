package com.casadosreclamos.repo

import com.casadosreclamos.model.request.Material
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import io.quarkus.panache.common.Parameters
import io.smallrye.mutiny.Uni
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class MaterialRepository: PanacheRepository<Material> {
    fun exists(name: String): Uni<Boolean>{
        return count("FROM Material m WHERE m.name = :name", Parameters.with("name", name).map()).onItem().transform { it != 0L }
    }
}