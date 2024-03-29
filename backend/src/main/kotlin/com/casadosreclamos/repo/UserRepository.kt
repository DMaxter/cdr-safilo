package com.casadosreclamos.repo

import com.casadosreclamos.model.User
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import io.quarkus.panache.common.Parameters
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class UserRepository : PanacheRepository<User> {
    fun findByEmailWithCredits(user: String): Uni<User> {
        return find(
            "FROM User u LEFT JOIN FETCH u.credits WHERE u.email = :user", Parameters.with("user", user).map()
        ).firstResult()
    }

    fun streamAllWithCredits(): Multi<User> {
        return stream("FROM User u LEFT JOIN FETCH u.credits")
    }

    fun exists(user: String): Uni<Boolean> {
        return count("FROM User u WHERE u.email = :user", Parameters.with("user", user).map()).onItem()
            .transform { it != 0L }
    }

    fun findByEmail(user: String): Uni<User> {
        return find("email = :user", Parameters.with("user", user).map()).firstResult()
    }
}