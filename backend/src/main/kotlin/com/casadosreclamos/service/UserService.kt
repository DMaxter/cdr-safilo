package com.casadosreclamos.service

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
import io.smallrye.mutiny.Multi
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

    fun getAllUsers(): Multi<User> {
        return userRepository.streamAllWithCredits()
    }

    fun getInfo(user: String): Uni<User> {
        return Panache.withTransaction {
            userRepository.findByEmailWithCredits(user)
        }
    }

    @Throws(InvalidCredentialsException::class)
    fun changePassword(username: String, old: String, new: String): Uni<Response> {
        return Panache.withTransaction {
            userRepository.findByEmail(username).onItem().transform { user ->
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
    fun setPlafond(userId: String?, brandId: Long, credits: Double): Uni<User> {
        if (userId.isNullOrEmpty()) {
            throw InvalidUserException()
        } else if (brandId <= 0) {
            throw InvalidIdException("brand")
        } else if (credits < 0) {
            throw InvalidAmountException()
        }

        val plafondId = PlafondId()
        var user: User? = null
        var brand: Brand? = null

        val userUni = userRepository.findByEmailWithCredits(userId)
        val brandUni = brandRepository.findById(brandId)

        return Panache.withTransaction {
            Uni.combine().all().unis(userUni, brandUni).asTuple().onItem().transformToUni { tuple ->
                user = tuple.item1 ?: throw InvalidUserException()
                brand = tuple.item2 ?: throw InvalidIdException("brand")

                plafondRepository.findById(user!!, brand!!)
            }.onItem().transformToUni { plafond ->
                if (plafond != null) {
                    plafond.amount = credits

                    Uni.createFrom().item(plafond)
                } else {
                    val newPlafond = Plafond()

                    plafondId.userId = user!!.name
                    plafondId.brandId = brand!!.id

                    newPlafond.amount = credits
                    newPlafond.id = plafondId
                    newPlafond.user = user!!
                    newPlafond.brand = brand!!

                    plafondRepository.persist(newPlafond)
                }
            }.onItem().transform { plafond ->
                user!!.credits.add(plafond)
                user
            }
        }
    }
}