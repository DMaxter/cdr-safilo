package com.casadosreclamos.service

import com.casadosreclamos.dto.UserDto
import com.casadosreclamos.repo.UserRepository
import io.quarkus.hibernate.reactive.panache.Panache
import io.smallrye.mutiny.Uni
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.core.Response

@ApplicationScoped
class UserService {
    @Inject
    lateinit var userRepository: UserRepository

    fun getInfo(user: String): Uni<Response> {
        return Panache.withTransaction {
            userRepository.findByName(user).onItem().transform { user -> Response.ok(UserDto(user.email, user.name, user.credits)).build() }
        }
    }
}