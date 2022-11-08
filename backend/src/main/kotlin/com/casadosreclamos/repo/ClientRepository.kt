package com.casadosreclamos.repo

import com.casadosreclamos.model.Client
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import io.quarkus.panache.common.Parameters
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ClientRepository : PanacheRepository<Client> {
    fun findByName(name: String): Uni<Client> {
        return find("name = :name", Parameters.with("name", name).map()).firstResult()
    }
}