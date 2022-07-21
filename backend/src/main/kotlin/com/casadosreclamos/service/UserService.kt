package com.casadosreclamos.service

import com.casadosreclamos.dto.UserDto
import com.casadosreclamos.exception.InvalidCredentialsException
import com.casadosreclamos.repo.UserRepository
import io.quarkus.elytron.security.common.BcryptUtil
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

    @Throws(InvalidCredentialsException::class)
    fun changePassword(user: String, old: String, new: String): Uni<Response> {
        return Panache.withTransaction {
            userRepository.findByName(user).onItem().transform { user ->
                return@transform if (BcryptUtil.matches(old, user.password)) {
                    user.password = BcryptUtil.bcryptHash(new)

                    Response.ok().build()
                } else {
                    throw InvalidCredentialsException()
                }
            }
        }
    }
}