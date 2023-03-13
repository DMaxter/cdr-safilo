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

    fun streamByBanner(banner: String): Multi<Client> {
        return stream("banner.name = :banner", Parameters.with("banner", banner).map())
    }

    override fun findById(id: Long?): Uni<Client> {
        return find(
            "FROM Client c LEFT JOIN FETCH c.images WHERE c.id = :id",
            Parameters.with("id", id).map()
        ).firstResult()
    }

    override fun streamAll(): Multi<Client> {
        return stream("FROM Client c LEFT JOIN FETCH c.images")
    }
}