package com.casadosreclamos.repo

import com.casadosreclamos.model.User
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import io.quarkus.panache.common.Parameters
import io.smallrye.mutiny.Uni
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class UserRepository : PanacheRepository<User> {
    fun findByName(user: String): Uni<User> {
        return find("email = :user", Parameters.with("user", user).map()).firstResult()
    }
}