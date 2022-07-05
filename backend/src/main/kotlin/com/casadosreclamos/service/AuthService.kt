package com.casadosreclamos.service

import com.casadosreclamos.dto.AuthDto
import com.casadosreclamos.exception.InvalidCredentialsException
import com.casadosreclamos.model.Role
import com.casadosreclamos.model.User
import com.casadosreclamos.repo.UserRepository
import io.quarkus.elytron.security.common.BcryptUtil
import io.quarkus.hibernate.reactive.panache.Panache
import io.smallrye.jwt.build.Jwt
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import org.bouncycastle.util.io.pem.PemReader
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.eclipse.microprofile.jwt.JsonWebToken
import org.jboss.logging.Logger
import java.io.InputStreamReader
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import javax.annotation.PostConstruct
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.NewCookie
import javax.ws.rs.core.Response

private const val ALGORITHM = "RSA"

@ApplicationScoped
class AuthService {
    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    @ConfigProperty(name = "mp.jwt.verify.issuer")
    lateinit var issuer: String

    @Inject
    @ConfigProperty(name = "mp.jwt.token.cookie")
    lateinit var cookie: String

    @Inject
    @ConfigProperty(name = "smallrye.jwt.sign.key.location")
    lateinit var keyPath: String

    lateinit var key: PrivateKey

    @PostConstruct
    fun init() {
        logger.info("Initializing Authentication Service")
        val file = AuthService::class.java.getResourceAsStream(keyPath)

        if (file != null) {
            logger.debug("Found key file")
                val content = PemReader(InputStreamReader(file)).readPemObject().content

                key = KeyFactory.getInstance(ALGORITHM).generatePrivate(PKCS8EncodedKeySpec(content))
                return
                // Do nothing
        }

        throw RuntimeException("No signing key found")
    }

    @Throws(InvalidCredentialsException::class)
    fun register(credentials: AuthDto): Uni<Response> {
        val user = User()

        if (credentials.username == null || credentials.password == null || credentials.username!!.isEmpty() || credentials.password!!.isEmpty()) {
            throw InvalidCredentialsException()
        }

        user.email = credentials.username!!
        user.password = BcryptUtil.bcryptHash(credentials.password!!)
        user.roles = mutableListOf(Role.COMMERCIAL)

        // TODO: send mail with credentials
        // TODO: check if user already registered

        return Panache.withTransaction { userRepository.persist(user) }.onItem().transform { Response.ok(user).build() }
            .onFailure().recoverWithItem { _ -> Response.serverError().build() }
    }

    fun getAll(): Multi<User> {
        return userRepository.streamAll()
    }

    @Throws(InvalidCredentialsException::class)
    fun login(credentials: AuthDto): Uni<Response> {

        if (credentials.username == null || credentials.password == null || credentials.username!!.isEmpty() || credentials.password!!.isEmpty()) {
            throw InvalidCredentialsException()
        }

        return Panache.withTransaction { userRepository.findByName(credentials.username!!) }
            .onItem().ifNull()
            .failWith { InvalidCredentialsException() }
            .onItem().ifNotNull()
            .transformToUni { user ->
                if (!BcryptUtil.matches(credentials.password!!, user.password)) {
                    Uni.createFrom().failure(InvalidCredentialsException())
                } else {
                    Uni.createFrom().item(user)
                }
            }
            .onItem()
            .transform { user: User -> Response.ok().cookie(NewCookie(cookie, generateJwt(user))).build() }
    }

    fun logout(): Uni<Response> {
        return Uni.createFrom().item(Response.ok().build())
    }

    fun forgot(@PathParam("username") user: String): Uni<Response> {
        return Uni.createFrom().item(Response.ok().build())
    }

    private fun generateJwt(user: User): String {
        return Jwt.issuer(issuer).claim("sub", user.email).claim("roles", user.roles).sign(key)
    }
}