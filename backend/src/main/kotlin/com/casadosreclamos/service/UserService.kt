package com.casadosreclamos.service

import com.casadosreclamos.dto.UserDto
import com.casadosreclamos.exception.InvalidAmountException
import com.casadosreclamos.exception.InvalidCredentialsException
import com.casadosreclamos.exception.InvalidIdException
import com.casadosreclamos.exception.InvalidUserException
import com.casadosreclamos.model.Plafond
import com.casadosreclamos.model.PlafondId
import com.casadosreclamos.model.User
import com.casadosreclamos.model.request.Brand
import com.casadosreclamos.repo.BrandRepository
import com.casadosreclamos.repo.PlafondRepository
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

    @Inject
    lateinit var plafondRepository: PlafondRepository

    @Inject
    lateinit var brandRepository: BrandRepository

    fun getInfo(user: String): Uni<Response> {
        return Panache.withTransaction {
            userRepository.findByNameWithCredits(user).onItem()
                .transform { user ->
                    Response.ok(UserDto(user)).build()
                }
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

    @Throws(InvalidIdException::class, InvalidUserException::class, InvalidAmountException::class)
    fun setPlafond(userId: String?, brandId: Long, credits: Double): Uni<Response> {
        if (userId == null || userId.isEmpty()) {
            throw InvalidUserException()
        } else if (brandId <= 0) {
            throw InvalidIdException("brand")
        } else if (credits < 0) {
            throw InvalidAmountException()
        }

        val plafondId = PlafondId()
        var user: User? = null
        var brand: Brand? = null

        return Panache.withTransaction {
            val userUni = userRepository.findByNameWithCredits(userId)
            val brandUni = brandRepository.findById(brandId)

            (Uni.combine().all().unis(userUni, brandUni).asTuple().onItem().transformToUni { tuple ->
                val userItem = tuple.item1 ?: throw InvalidUserException()
                val brandItem = tuple.item2 ?: throw InvalidIdException("brand")

                user = userItem
                brand = brandItem

                plafondId.userId = user!!.name
                plafondId.brandId = brand!!.id

                plafondRepository.findById(plafondId)
            }).onItem().ifNull().switchTo {
                val plafond = Plafond()
                plafond.amount = credits
                plafond.id = plafondId
                plafond.user = user!!
                plafond.brand = brand!!

                user!!.credits.add(plafond)

                plafondRepository.persist(plafond)
            }.onItem().ifNotNull().transform { plafond ->
                plafond.amount = credits
                plafond
            }
        }.onItem().transform { Response.ok().build() }
    }
}