package com.casadosreclamos.repo

import com.casadosreclamos.model.PasswordToken
import com.casadosreclamos.model.PasswordTokenId
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase
import io.quarkus.panache.common.Parameters
import io.smallrye.mutiny.Uni
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class PasswordTokenRepository : PanacheRepositoryBase<PasswordToken, PasswordTokenId> {
    fun findById(user: String, token: String): Uni<PasswordToken> {
        return find(
            "user = :user and token = :token", Parameters.with("user", user).and("token", token).map()
        ).firstResult()
    }
}