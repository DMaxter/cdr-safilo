package com.casadosreclamos.service

import com.casadosreclamos.EMAIL_REGEX
import com.casadosreclamos.dto.AuthDto
import com.casadosreclamos.dto.RegisterDto
import com.casadosreclamos.dto.UserDto
import com.casadosreclamos.exception.*
import com.casadosreclamos.getEmailPasswordRecovery
import com.casadosreclamos.getEmailRegisteredCommercial
import com.casadosreclamos.model.PasswordToken
import com.casadosreclamos.model.PasswordTokenId
import com.casadosreclamos.model.Role
import com.casadosreclamos.model.User
import com.casadosreclamos.repo.PasswordTokenRepository
import com.casadosreclamos.repo.UserRepository
import io.quarkus.elytron.security.common.BcryptUtil
import io.quarkus.hibernate.reactive.panache.Panache
import io.quarkus.mailer.Mail
import io.quarkus.mailer.reactive.ReactiveMailer
import io.quarkus.runtime.Startup
import io.quarkus.runtime.configuration.ProfileManager
import io.smallrye.jwt.build.Jwt
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import org.bouncycastle.util.io.pem.PemReader
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.jboss.logging.Logger
import java.io.File
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
private const val ADMIN_MAIL = "admin"
private const val ADMIN_PASS = "S4f!l0CDR"

private const val CDR_NAME = "CDR"
private const val CDR_MAIL = "CDR"
private const val CDR_PASS = "123"

private const val SAFILO_NAME = "safilo"
private const val SAFILO_MAIL = "safilo"
private const val SAFILO_PASS = "123"

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
    lateinit var mailer: ReactiveMailer

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
            ADMIN_NAME, ADMIN_MAIL, BcryptUtil.bcryptHash(ADMIN_PASS), mutableSetOf(Role.ADMIN)
        )
        admin.credits = mutableSetOf()

        Panache.withTransaction {
            userRepository.findByEmail(ADMIN_NAME).onItemOrFailure().transformToUni { user: User?, fail: Throwable? ->
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

        logger.info("Creating CDR user")

        val cdr = User(
            CDR_NAME, CDR_MAIL, BcryptUtil.bcryptHash(CDR_PASS), mutableSetOf(Role.CDR)
        )
        cdr.credits = mutableSetOf()

        Panache.withTransaction {
            userRepository.findByEmail(CDR_MAIL).onItemOrFailure().transformToUni { user: User?, fail: Throwable? ->
                if (user == null) {
                    return@transformToUni userRepository.persist(cdr).onItemOrFailure()
                        .invoke { _: User?, error: Throwable? ->
                            if (fail == null) {
                                logger.info("CDR user created successfully")
                            } else {
                                logger.error("Failed to create CDR user: $error")
                            }
                        }
                } else if (fail != null) {
                    logger.error("Failed to find CDR user: $fail")
                } else {
                    logger.warn("CDR user already created")
                }

                return@transformToUni Uni.createFrom().nullItem()
            }
        }.await().indefinitely()

        logger.info("Creating Safilo user")

        val safilo = User(
            SAFILO_NAME, SAFILO_MAIL, BcryptUtil.bcryptHash(SAFILO_PASS), mutableSetOf(Role.MANAGER)
        )
        safilo.credits = mutableSetOf()

        Panache.withTransaction {
            userRepository.findByEmail(SAFILO_MAIL).onItemOrFailure().transformToUni { user: User?, fail: Throwable? ->
                if (user == null) {
                    return@transformToUni userRepository.persist(safilo).onItemOrFailure()
                        .invoke { _: User?, error: Throwable? ->
                            if (fail == null) {
                                logger.info("Safilo user created successfully")
                            } else {
                                logger.error("Failed to create Safilo user: $error")
                            }
                        }
                } else if (fail != null) {
                    logger.error("Failed to find Safilo user: $fail")
                } else {
                    logger.warn("Safilo user already created")
                }

                return@transformToUni Uni.createFrom().nullItem()
            }
        }.await().indefinitely()

        logger.info("Initializing Authentication Service")
        val file =
            if (ProfileManager.getActiveProfile() == "dev" || ProfileManager.getActiveProfile() == "test") AuthService::class.java.getResourceAsStream(
                keyPath
            ) else File(
                keyPath
            ).inputStream()

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
    fun register(credentials: RegisterDto): Uni<User> {
        logger.info("Registering user $credentials")

        val user = User()

        if (credentials.email == null || credentials.password == null || credentials.email!!.isEmpty() || credentials.password!!.isEmpty()) {
            logger.error("Email or password are null or empty")
            throw InvalidCredentialsException()
        } else if (!EMAIL_REGEX.matches(credentials.email!!)) {
            logger.error("Email not in valid format")
            throw InvalidEmailException()
        } else if (credentials.name == null || credentials.name!!.isEmpty()) {
            logger.error("Name is null or empty")
            throw InvalidNameException()
        }

        user.email = credentials.email!!
        user.password = BcryptUtil.bcryptHash(credentials.password!!)
        user.roles = mutableSetOf(Role.COMMERCIAL)
        user.name = credentials.name!!
        user.credits = mutableSetOf()

        return Panache.withTransaction {
            userRepository.exists(user.email).onItem().transformToUni { value ->
                return@transformToUni if (value) {
                    logger.error("The user with email ${user.email} is already registered")
                    throw AlreadyExistsException("User")
                } else {
                    userRepository.persist(user).onItem().invoke { _ -> logger.info("User ${user.name} created") }
                        .onFailure().invoke { e -> logger.error("Couldn't persist user ${user.email}: $e") }
                }
            }
        }.onItem().ifNotNull().transformToUni { _ ->
            mailer.send(
                Mail.withText(
                    user.email, "Registo na plataforma Safilo/CDR", getEmailRegisteredCommercial(credentials)
                )
            ).onItem().invoke { _ ->
                logger.info("Mail with credentials sent to ${user.email}")
            }.onFailure().invoke { e ->
                logger.error("Couldn't send email with credentials: $e")
            }
        }.onItem().transform { _ ->
            logger.info("Successfully registered user")

            user
        }
    }

    fun getAll(): Multi<UserDto> {
        return userRepository.streamAllWithCredits().map { UserDto(it) }
    }

    @Throws(InvalidCredentialsException::class)
    fun login(credentials: AuthDto): Uni<Response> {
        logger.info("Logging in with ${credentials}")

        if (credentials.email == null || credentials.password == null || credentials.email!!.isEmpty() || credentials.password!!.isEmpty()) {
            logger.error("Email or password are null or empty")
            throw InvalidCredentialsException()
        }

        return Panache.withTransaction { userRepository.findByEmail(credentials.email!!) }.onItem()
            .transformToUni { user ->
                if (user == null) {
                    logger.error("User ${credentials.email} is not registered")

                    throw InvalidCredentialsException()
                } else if (!BcryptUtil.matches(credentials.password!!, user.password)) {
                    logger.error("Password doesn't match for user ${credentials.email}")

                    Uni.createFrom().failure(InvalidCredentialsException())
                } else {
                    Uni.createFrom().item(user)
                }
            }.onItem().transform { user: User ->
                logger.info("Successfully logged in")

                Response.ok().cookie(
                    NewCookie(
                        cookie, generateJwt(user), "/", domain, "", Duration.ofHours(TIME).seconds.toInt(), true, true
                    )
                ).build()
            }
    }

    fun logout(): Uni<Response> {
        // Unset cookie by setting Max-Age to 0
        logger.info("Successfully logged out")

        return Uni.createFrom()
            .item(Response.ok().cookie(NewCookie(cookie, "", "/", domain, "", 0, true, true)).build())
    }

    @Throws(InvalidUserException::class)
    fun forgot(username: String): Uni<Response> {
        if (username.isEmpty()) {
            logger.error("Empty username")
            throw InvalidUserException()
        }

        // Generate random alphanumeric token
        val userToken = SecureRandom().ints(48, 123).filter { (it <= 57 || it >= 64) && (it <= 90 || it >= 97) }.limit(
            TOKEN_LEN
        ).collect(::StringBuilder, StringBuilder::appendCodePoint, StringBuilder::append).toString()

        return Panache.withTransaction {
            userRepository.findByEmail(username).onItem().transformToUni { user ->
                if (user != null) {
                    val token = PasswordToken()
                    token.id = PasswordTokenId()
                    token.id.user = user.email

                    // Store only hash of token
                    token.id.token = sha512(userToken.toByteArray())

                    // Set expiry to 2 days
                    val curDate = Date()
                    val calendar = Calendar.getInstance()
                    calendar.time = curDate
                    calendar.add(Calendar.DATE, 2)

                    token.expiry = calendar.time

                    logger.info("Generated token $userToken")

                    tokenRepository.persist(token).onItem().transformToUni { _ ->
                        logger.info("Token for $user created successfully")

                        mailer.send(
                            Mail.withText(
                                username,
                                "Pedido de substituição de password para a plataforma Safilo/CDR",
                                getEmailPasswordRecovery(domain, user.name, userToken)
                            )
                        ).onItem().invoke { _ -> logger.info("Sent email to $username with token") }.onFailure()
                            .invoke { e -> logger.error("Couldn't send email with token: $e") }
                    }.onFailure().invoke { e -> logger.error("Couldn't persist token: $e") }
                } else {
                    logger.error("User $user is not registered")

                    Uni.createFrom().nullItem()
                }
            }
        }.onItem().transform { _ ->
            Response.ok().build()
        }
    }

    @Throws(InvalidTokenException::class, InvalidPasswordException::class)
    fun changePassword(username: String, password: String, tryToken: String): Uni<Response> {
        logger.info("Using token $tryToken")

        if (password.isEmpty()) {
            logger.error("Password is empty")

            throw InvalidPasswordException()
        } else if (username.isEmpty()) {
            logger.error("Username is empty")

            throw InvalidNameException()
        } else if (tryToken.isEmpty()) {
            logger.error("Token is empty")

            throw InvalidTokenException()
        }

        val tokenHashed = sha512(tryToken.toByteArray())

        return Panache.withTransaction {
            val userUni = userRepository.findByEmail(username)
            val tokenUni = tokenRepository.findById(username, tokenHashed)

            Uni.combine().all().unis(userUni, tokenUni).asTuple().onItem().transformToUni { tuple ->
                val user = tuple.item1
                val token = tuple.item2

                logger.info("Fetched user ${user}")
                logger.info("Fetched token ${token}")

                if (user == null || token == null) {
                    logger.error("Username or token doesn't exist")

                    throw InvalidTokenException()
                }

                user.password = BcryptUtil.bcryptHash(password)

                logger.info("Changed password")

                // Delete token
                tokenRepository.delete(token)
            }.onItem().transform {
                logger.info("Deleted token")

                Response.ok().build()
            }.onFailure().invoke { e -> logger.error("Couldn't delete token: $e") }
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