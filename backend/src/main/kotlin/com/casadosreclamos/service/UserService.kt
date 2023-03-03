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
import org.jboss.logging.Logger
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.core.Response

@ApplicationScoped
class UserService {
    @Inject
    lateinit var logger: Logger

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
        if (new.isBlank()) {
            logger.error("Password is empty")

            throw InvalidCredentialsException()
        }

        return Panache.withTransaction {
            userRepository.findByEmail(username).onItem().transform { user ->
                return@transform if (BcryptUtil.matches(old, user.password)) {
                    user.password = BcryptUtil.bcryptHash(new)

                    logger.info("Successfully changed password")

                    Response.ok().build()
                } else {
                    logger.error("Password doesn't match")

                    throw InvalidCredentialsException()
                }
            }
        }
    }

    @Throws(InvalidIdException::class, InvalidUserException::class, InvalidAmountException::class)
    fun setPlafond(userId: String?, brandId: Long, credits: Double): Uni<User> {
        if (userId.isNullOrEmpty()) {
            logger.error("User ID is null or empty")

            throw InvalidUserException()
        } else if (brandId <= 0) {
            logger.error("Brand ID is invalid")

            throw InvalidIdException("brand")
        } else if (credits < 0) {
            logger.error("Plafond is invalid")

            throw InvalidAmountException()
        }

        val plafondId = PlafondId()
        var user: User? = null
        var brand: Brand? = null

        val userUni = userRepository.findByEmailWithCredits(userId)
        val brandUni = brandRepository.findById(brandId)

        return Panache.withTransaction {
            Uni.combine().all().unis(userUni, brandUni).asTuple().onItem().transformToUni { tuple ->
                user = tuple.item1
                brand = tuple.item2

                if (user == null) {
                    logger.error("User with ID ${userId} is not registered")

                    throw InvalidUserException()
                } else if (brand == null) {
                    logger.error("Brand with ID ${brandId} is not registered")

                    throw InvalidIdException("brand")
                }

                plafondRepository.findById(user!!, brand!!)
            }.onItem().transformToUni { plafond ->
                if (plafond != null) {

                    plafond.amount = credits

                    logger.info("Successfully change existing plafond")

                    Uni.createFrom().item(plafond)
                } else {
                    logger.info("Creating new plafond entry")

                    val newPlafond = Plafond()

                    plafondId.userId = user!!.name
                    plafondId.brandId = brand!!.id

                    newPlafond.amount = credits
                    newPlafond.id = plafondId
                    newPlafond.user = user!!
                    newPlafond.brand = brand!!

                    plafondRepository.persist(newPlafond).onItem()
                        .invoke { _ -> logger.info("Successfully created plafond") }.onFailure()
                        .invoke { e -> logger.error("Couldn't register plafond: $e") }
                }
            }.onItem().transform { plafond ->
                user!!.credits.add(plafond)

                logger.info("Added plafond to user")

                user
            }
        }
    }
}