package com.casadosreclamos.service

import com.casadosreclamos.dto.AuthDto
import com.casadosreclamos.dto.RegisterDto
import com.casadosreclamos.dto.UserDto
import com.casadosreclamos.exception.*
import com.casadosreclamos.model.PasswordToken
import com.casadosreclamos.model.PasswordTokenId
import com.casadosreclamos.model.Role
import com.casadosreclamos.model.User
import com.casadosreclamos.repo.PasswordTokenRepository
import com.casadosreclamos.repo.UserRepository
import io.quarkus.elytron.security.common.BcryptUtil
import io.quarkus.hibernate.reactive.panache.Panache
import io.quarkus.runtime.Startup
import io.smallrye.jwt.build.Jwt
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import org.bouncycastle.util.io.pem.PemReader
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.jboss.logging.Logger
import java.io.InputStreamReader
import java.security.KeyFactory
import java.security.MessageDigest
import java.security.PrivateKey
import java.security.SecureRandom
import java.security.spec.PKCS8EncodedKeySpec
import java.time.Duration
import java.util.*
import javax.annotation.PostConstruct
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.core.NewCookie
import javax.ws.rs.core.Response

// JWT Configuration
private const val ALGORITHM = "RSA"
private const val TOKEN_LEN: Long = 64

// Default user credentials
private const val ADMIN_NAME = "admin"
private const val ADMIN_PASS = "S4f!l0CDR"

private const val TIME: Long = 3 // hours

@Startup
@ApplicationScoped
class AuthService {
    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var tokenRepository: PasswordTokenRepository

    @Inject
    @ConfigProperty(name = "mp.jwt.verify.issuer")
    lateinit var issuer: String

    @Inject
    @ConfigProperty(name = "mp.jwt.token.cookie")
    lateinit var cookie: String

    @Inject
    @ConfigProperty(name = "smallrye.jwt.sign.key.location")
    lateinit var keyPath: String

    @Inject
    @ConfigProperty(name = "domain.name")
    lateinit var domain: String

    lateinit var key: PrivateKey

    @PostConstruct
    fun init() {
        logger.info("Creating admin user")

        val admin = User(
            "Admin", ADMIN_NAME, BcryptUtil.bcryptHash(ADMIN_PASS), mutableSetOf(Role.ADMIN)
        )

        Panache.withTransaction {
            userRepository.findByName(ADMIN_NAME).onItemOrFailure().transformToUni { user: User?, fail: Throwable? ->
                if (user == null) {
                    return@transformToUni userRepository.persist(admin).onItemOrFailure()
                        .invoke { _: User?, error: Throwable? ->
                            if (fail == null) {
                                logger.info("Admin created successfully")
                            } else {
                                logger.error("Failed to create admin: $error")
                            }
                        }
                } else if (fail != null) {
                    logger.error("Failed to find admin: $fail")
                } else {
                    logger.warn("Admin already created")
                }

                return@transformToUni Uni.createFrom().nullItem()
            }
        }.await().indefinitely()

        logger.info("Initializing Authentication Service")
        val file = AuthService::class.java.getResourceAsStream(keyPath)

        if (file != null) {
            logger.debug("Found key file")
            val content = PemReader(InputStreamReader(file)).readPemObject().content

            key = KeyFactory.getInstance(ALGORITHM).generatePrivate(PKCS8EncodedKeySpec(content))

            logger.info("Authentication Service initialized successfully")

            return
            // Do nothing
        }

        logger.error("Couldn't find a signing key for JWT")
        throw RuntimeException("No signing key found")
    }

    @Throws(InvalidCredentialsException::class)
    fun register(credentials: RegisterDto): Uni<Response> {
        val user = User()

        if (credentials.email == null || credentials.password == null || credentials.email!!.isEmpty() || credentials.password!!.isEmpty()) {
            throw InvalidCredentialsException()
        } else if (credentials.name == null || credentials.name!!.isEmpty()) {
            throw InvalidNameException()
        }

        // TODO: verify valid email

        user.email = credentials.email!!
        user.password = BcryptUtil.bcryptHash(credentials.password!!)
        user.roles = mutableSetOf(Role.COMMERCIAL)
        user.name = credentials.name!!

        // TODO: send mail with credentials
        // TODO: check if user already registered

        return Panache.withTransaction { userRepository.persist(user) }.onItem().transform { Response.ok(user).build() }
            .onFailure().recoverWithItem { e ->
                logger.error(e)
                Response.serverError().build()
            }
    }

    fun getAll(): Multi<UserDto> {
        return userRepository.streamAll().map { UserDto(it) }
    }

    @Throws(InvalidCredentialsException::class)
    fun login(credentials: AuthDto): Uni<Response> {

        if (credentials.email == null || credentials.password == null || credentials.email!!.isEmpty() || credentials.password!!.isEmpty()) {
            throw InvalidCredentialsException()
        }

        return Panache.withTransaction { userRepository.findByName(credentials.email!!) }.onItem().ifNull()
            .failWith { InvalidCredentialsException() }.onItem().ifNotNull().transformToUni { user ->
                if (!BcryptUtil.matches(credentials.password!!, user.password)) {
                    Uni.createFrom().failure(InvalidCredentialsException())
                } else {
                    Uni.createFrom().item(user)
                }
            }.onItem().transform { user: User ->
                Response.ok().cookie(
                    NewCookie(
                        cookie, generateJwt(user), "/", domain, "",
                        Duration.ofHours(TIME).seconds.toInt(), true, true
                    )
                ).build()
            }
    }

    fun logout(): Uni<Response> {
        // Unset cookie by setting Max-Age to 0
        return Uni.createFrom()
            .item(Response.ok().cookie(NewCookie(cookie, "", "/", domain, "", 0, true, true)).build())
    }

    @Throws(InvalidUserException::class)
    fun forgot(user: String): Uni<Response> {
        if (user.isEmpty()) {
            throw InvalidUserException()
        }

        return Panache.withTransaction {
            userRepository.findByName(user).onItem().ifNotNull().transformToUni { user ->
                val token = PasswordToken()
                token.id = PasswordTokenId()
                token.id.user = user.email

                // Generate random alphanumeric token
                val userToken =
                    SecureRandom().ints(48, 123).filter { (it <= 57 || it >= 64) && (it <= 90 || it >= 97) }.limit(
                        TOKEN_LEN
                    ).collect(::StringBuilder, StringBuilder::appendCodePoint, StringBuilder::append).toString()

                // Store only hash of token
                token.id.token = sha512(userToken.toByteArray())

                // Set expiry to 2 days
                val curDate = Date()
                val calendar = Calendar.getInstance()
                calendar.time = curDate
                calendar.add(Calendar.DATE, 2)

                token.expiry = calendar.time

                logger.warn(userToken)

                // TODO: Send mail with token

                tokenRepository.persist(token)
            }.onItem().transform { Response.ok().build() }
        }
    }

    @Throws(InvalidTokenException::class, InvalidPasswordException::class)
    fun changePassword(username: String, password: String, tryToken: String): Uni<Response> {
        if (password.isEmpty()) {
            throw InvalidPasswordException()
        } else if (username.isEmpty() || tryToken.isEmpty()) {
            throw InvalidTokenException()
        }

        val tokenHashed = sha512(tryToken.toByteArray())

        return Panache.withTransaction {
            val userUni = userRepository.findByName(username)
            val tokenUni = tokenRepository.findById(username, tokenHashed)

            Uni.combine().all().unis(userUni, tokenUni).asTuple().onItem().transformToUni { tuple ->
                val user = tuple.item1
                val token = tuple.item2

                if (user == null || token == null) {
                    throw InvalidTokenException()
                }

                user.password = BcryptUtil.bcryptHash(password)

                // Delete token
                tokenRepository.delete(token)
            }.onItem().transform { Response.ok().build() }
        }
    }

    private fun generateJwt(user: User): String {
        return Jwt.issuer(issuer).claim("sub", user.email).claim("roles", user.roles).expiresIn(Duration.ofHours(TIME))
            .sign(key)
    }

    private fun sha512(bytes: ByteArray): String {
        val digest = MessageDigest.getInstance("SHA-512")
        return printableHexString(digest.digest(bytes))
    }

    private fun printableHexString(hash: ByteArray): String {
        return hash.map { Integer.toHexString(0xFF and it.toInt()) }.map { if (it.length < 2) "0$it" else it }
            .fold("") { back, new -> back + new }
    }
}