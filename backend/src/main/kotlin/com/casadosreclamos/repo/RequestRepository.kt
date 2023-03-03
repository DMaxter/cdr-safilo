package com.casadosreclamos.repo

import com.casadosreclamos.model.Request
import com.casadosreclamos.model.request.Material
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import io.quarkus.panache.common.Parameters
import io.smallrye.mutiny.Multi
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class RequestRepository: PanacheRepository<Request> {
    fun streamAll(ids: List<Long>): Multi<Request> {
        return stream("FROM Request r WHERE r.id IN :list", Parameters.with("list", ids).map())
    }

    fun streamByBanner(banner: String): Multi<Request> {
        return stream("client.banner.name = :name", Parameters.with("name", banner).map())
    }
}