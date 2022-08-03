package com.casadosreclamos.repo

import com.casadosreclamos.model.Client
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import io.quarkus.panache.common.Parameters
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ClientRepository : PanacheRepository<Client> {
    fun streamAllWithAddresses(): Multi<Client> {
        return stream("FROM Client c LEFT JOIN FETCH c.addresses")
    }

    fun findByName(name: String): Uni<Client> {
        return find("name = :name", Parameters.with("name", name).map()).firstResult()
    }

    fun findByIdWithAddresses(id: Long): Uni<Client> {
        return find(
            "FROM Client c LEFT JOIN FETCH c.addresses WHERE c.id = :id",
            Parameters.with("id", id).map()
        ).firstResult()
    }
}