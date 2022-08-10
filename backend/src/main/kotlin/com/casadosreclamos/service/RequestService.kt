package com.casadosreclamos.service

import com.casadosreclamos.dto.NewRequestDto
import com.casadosreclamos.dto.OneFace as OneDto
import com.casadosreclamos.dto.TwoFaces as TwoDto
import com.casadosreclamos.dto.LeftShowcase as LeftDto
import com.casadosreclamos.dto.RightShowcase as RightDto
import com.casadosreclamos.dto.RequestDto
import com.casadosreclamos.dto.RequestTypeDto
import com.casadosreclamos.dto.ShowcaseDto
import com.casadosreclamos.exception.*
import com.casadosreclamos.model.Request
import com.casadosreclamos.model.Role
import com.casadosreclamos.model.request.*
import com.casadosreclamos.repo.*
import io.quarkus.hibernate.reactive.panache.Panache
import io.quarkus.mailer.Mail
import io.quarkus.mailer.reactive.ReactiveMailer
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.jboss.logging.Logger
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.core.Response
import javax.ws.rs.core.Response.Status

private const val CDR_EMAIL = "daniel99matos@gmail.com"

@ApplicationScoped
class RequestService {
    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var requestRepository: RequestRepository

    @Inject
    lateinit var clientRepository: ClientRepository

    @Inject
    lateinit var brandRepository: BrandRepository

    @Inject
    lateinit var materialRepository: MaterialRepository

    @Inject
    lateinit var requestInfoRepository: RequestInfoRepository

    @Inject
    lateinit var requestTypeRepository: RequestTypeRepository

    @Inject
    lateinit var imageRepository: ImageRepository

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var mailer: ReactiveMailer

    @Inject
    @ConfigProperty(name = "cdr.email")
    lateinit var cdrMail: String

    fun getAll(): Multi<RequestDto> {
        return requestRepository.streamAll().map { RequestDto(it) }
    }

    @Throws(InvalidIdException::class, InvalidMeasurementsException::class, InvalidRequestTypeException::class)
    fun add(requestDto: NewRequestDto, username: String): Uni<Response> {
        if (requestDto.clientId == null || requestDto.clientId!! <= 0) {
            throw InvalidIdException("client")
        } else if (requestDto.brandId == null || requestDto.brandId!! <= 0) {
            throw InvalidIdException("brand")
        } else if (requestDto.materialId == null || requestDto.materialId!! <= 0) {
            throw InvalidIdException("material")
        } else if (requestDto.measurements == null || requestDto.measurements!!.height <= 0 || requestDto.measurements!!.width <= 0) {
            throw InvalidMeasurementsException()
        } else if (requestDto.amount == null || requestDto.amount!! <= 0) {
            throw InvalidAmountException()
        } else if (requestDto.images == null) {
            throw InvalidRequestTypeException()
        }

        val request = Request()
        request.status = RequestStatus.ORDERED
        request.created = Date()
        request.lastUpdate = request.created
        request.observations = requestDto.observations

        val requestInfo = RequestInfo()
        requestInfo.measurement = requestDto.measurements!!
        requestInfo.amount = requestDto.amount!!

        lateinit var materialName: String
        lateinit var clientName: String
        lateinit var userName: String
        lateinit var brandName: String

        return Panache.withTransaction {
            val clientUni = clientRepository.findByIdWithAddresses(requestDto.clientId!!).onFailure()
                .transform { InvalidIdException("client") }
            val brandUni =
                brandRepository.findById(requestDto.brandId!!).onFailure().transform { InvalidIdException("brand") }
            val materialUni = materialRepository.findById(requestDto.materialId!!).onFailure()
                .transform { InvalidIdException("material") }
            val userUni = userRepository.findByName(username)

            Uni.combine().all().unis(clientUni, brandUni, materialUni, userUni).asTuple().onItem()
                .transformToUni { tuple ->
                    val client = tuple.item1
                    val brand = tuple.item2
                    val material = tuple.item3
                    val user = tuple.item4

                    if (client == null) {
                        throw InvalidIdException("client")
                    } else if (brand == null) {
                        throw InvalidIdException("brand")
                    } else if (material == null) {
                        throw InvalidIdException("material")
                    } else if (client.addresses.stream().noneMatch { it.id == requestDto.addressId }) {
                        throw InvalidIdException("address")
                    }

                    materialName = material.name
                    clientName = client.name
                    userName = user.name
                    brandName = brand.name

                    // TODO: check plafond
                    // TODO: Check if image from brand

                    request.address =
                        client.addresses.stream().filter { it.id == requestDto.addressId }.findFirst().get()
                    request.requester = user
                    request.client = client
                    requestInfo.brand = brand
                    requestInfo.material = material

                    requestRepository.persistAndFlush(request)
                }.onItem().transformToUni { _ ->
                    requestInfo.request = request
                    request.info = requestInfo
                    requestInfoRepository.persist(requestInfo)
                }.onItem().transformToUni { _ ->
                    toRequestType(requestDto.images!!)
                }.onItem().transformToUni { requestType ->
                    requestType.request = requestInfo
                    requestInfo.type = requestType
                    requestTypeRepository.persist(requestType)
                }
        }.onItem().transformToUni { _ ->
            mailer.send(
                Mail.withText(
                    cdrMail, "Novo pedido efetuado por comercial da Safilo", """
                    Foi efetuado um novo pedido à Casa dos Reclamos pelo utilizador.
                    Resumo do pedido:
                    
                    Comercial: $userName
                    Cliente: $clientName
                    Material: $materialName
                    Marca: $brandName
                    Tipo de pedido: ${getType(requestDto.images!!)}
                    
                    Para ver todos os detalhes aceda à plataforma
                    
                    
                    Este email é automático, por favor não responda 
                    """.trimIndent()
                )
            )
        }.onItem().transform {
            Response.ok().build()
        }
    }

    fun toProduction(requestId: Long): Uni<Response> {
        return Panache.withTransaction {
            requestRepository.findById(requestId).onItem().ifNotNull().transform { request ->
                request.status = RequestStatus.IN_PRODUCTION
                request.lastUpdate = Date()

                Response.ok().build()
            }.onItem().ifNull().continueWith {
                logger.error("Request not found")
                Response.status(Status.NOT_FOUND).build()
            }
        }
    }

    fun finishRequest(requestId: Long, code: String?): Uni<Response> {
        return Panache.withTransaction {
            requestRepository.findById(requestId).onItem().ifNotNull().transform { request ->
                request.status = RequestStatus.DONE
                request.lastUpdate = Date()
                request.trackingCode = code

                Response.ok().build()
            }.onItem().ifNull().continueWith {
                logger.error("Request not found")
                Response.status(Status.NOT_FOUND).build()
            }
        }
    }

    fun cancel(requestId: Long, username: String): Uni<Response> {
        lateinit var roles: Set<Role>

        // TODO: Send mail

        return Panache.withTransaction {
            userRepository.findByName(username).onItem().transformToUni { user ->
                roles = user.roles

                requestRepository.findById(requestId).onItem().ifNotNull().transform { request ->
                    // Prevent other commercials from cancelling arbitrary requests
                    if (roles.contains(Role.ADMIN) || roles.contains(Role.CDR) || roles.contains(Role.MANAGER) || (roles.contains(
                            Role.COMMERCIAL
                        ) && request.requester.name == username)
                    ) {
                        request.status = RequestStatus.CANCELLED
                        request.lastUpdate = Date()
                    } else {
                        throw OperationNotPerformedException()
                    }

                    Response.ok().build()
                }.onItem().ifNull().continueWith {
                    logger.error("Request not found")
                    Response.status(Status.NOT_FOUND).build()
                }
            }
        }
    }

    private fun toRequestType(request: RequestTypeDto): Uni<out RequestType> {
        return when (request) {
            is OneDto -> toOneFace(request)
            is TwoDto -> toTwoFaces(request)
            is LeftDto -> toLeftShowcase(request)
            is RightDto -> toRightShowcase(request)
            else -> throw InvalidRequestTypeException()
        }
    }

    private fun toOneFace(request: OneDto): Uni<OneFace> {
        return imageRepository.findById(request.cover.id).onItem().transform { image ->
            val type = OneFace()
            type.cover = image
            type
        }.onFailure().transform { InvalidIdException("image") }
    }

    private fun toTwoFaces(request: TwoDto): Uni<TwoFaces> {
        val coverUni = imageRepository.findById(request.cover.id)
        val backUni = imageRepository.findById(request.back.id)

        return Uni.join().all(coverUni, backUni).andFailFast().onItem().transform { (cover, back) ->
            val type = TwoFaces()
            type.cover = cover
            type.back = back
            type
        }.onFailure().transform { InvalidIdException("image") }
    }

    private fun toShowcase(request: ShowcaseDto, output: Showcase): Uni<Showcase> {
        val topUni = imageRepository.findById(request.top.id)
        val bottomUni = imageRepository.findById(request.bottom.id)
        val leftUni = imageRepository.findById(request.left.id)
        val rightUni = imageRepository.findById(request.right.id)
        val sideUni = imageRepository.findById(request.side.id)

        return Uni.join().all(topUni, bottomUni, leftUni, rightUni, sideUni).andFailFast().onItem()
            .transform { (top, bottom, left, right, side) ->
                output.top = top
                output.bottom = bottom
                output.left = left
                output.right = right
                output.side = side

                output
            }.onFailure().transform { InvalidIdException("image") }
    }

    private fun toLeftShowcase(request: LeftDto): Uni<Showcase> {
        val showcase = LeftShowcase()

        return toShowcase(request, showcase)
    }

    private fun toRightShowcase(request: RightDto): Uni<Showcase> {
        val showcase = RightShowcase()

        return toShowcase(request, showcase)
    }

    private fun getType(request: RequestTypeDto): String {
        return when (request) {
            is OneDto -> "Uma face"
            is TwoDto -> "Duas faces"
            is ShowcaseDto -> "Montra"
            else -> throw InvalidRequestTypeException()
        }
    }
}
