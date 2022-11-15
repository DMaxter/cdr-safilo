package com.casadosreclamos.service

import com.casadosreclamos.dto.*
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
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.jboss.logging.Logger
import java.io.File
import java.io.FileWriter
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.core.Response
import javax.ws.rs.core.Response.Status
import com.casadosreclamos.dto.LeftShowcase as LeftDto
import com.casadosreclamos.dto.OneFace as OneDto
import com.casadosreclamos.dto.RightShowcase as RightDto
import com.casadosreclamos.dto.TwoFaces as TwoDto

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
    lateinit var requestSlotRepository: RequestSlotRepository

    @Inject
    lateinit var requestTypeRepository: RequestTypeRepository

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
        } else if (requestDto.amount == null || requestDto.amount!! <= 0) {
            throw InvalidAmountException()
        } else if (requestDto.type == null) {
            throw InvalidRequestTypeException()
        } else if (requestDto.application == null) {
            requestDto.application = false
        }

        validateType(requestDto.type!!)

        val request = Request()
        request.status = RequestStatus.ORDERED
        request.created = Date()
        request.lastUpdate = request.created
        request.observations = requestDto.observations
        request.application = requestDto.application!!
        request.amount = requestDto.amount!!

        lateinit var clientName: String
        lateinit var userName: String

        return Panache.withTransaction {
            val clientUni = clientRepository.findById(requestDto.clientId!!)
            val userUni = userRepository.findByName(username)

            Uni.combine().all().unis(clientUni, userUni).asTuple().onItem().transformToUni { tuple ->
                // Obtain client and user
                val client = tuple.item1
                val user = tuple.item2

                if (client == null) {
                    throw InvalidIdException("client")
                }

                clientName = client.name
                userName = user.name

                // TODO: check plafond

                request.requester = user
                request.client = client

                requestRepository.persistAndFlush(request)
            }.onItem().transformToUni { _ ->
                // Create RequestType instance
                toRequestType(requestDto.type!!)
            }.onItem().transformToUni { requestType ->
                // Create request
                requestType.request = request
                request.type = requestType

                requestTypeRepository.persist(requestType)
            }
        }.onItem().transformToUni { _ ->
            // Send email
            mailer.send(
                Mail.withText(
                    cdrMail, "Novo pedido efetuado por comercial da Safilo", """
                    Foi efetuado um novo pedido à Casa dos Reclamos pelo utilizador.
                    Resumo do pedido:
                    
                    Comercial: $userName
                    Cliente: $clientName
                    Tipo de pedido: ${getType(requestDto.type!!)}
                    
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

    fun exportBanner(banner: String): Uni<File> {
        val file = kotlin.io.path.createTempFile().toFile()
        val writer = CSVPrinter(FileWriter(file), CSVFormat.EXCEL)

        val bannerRequests = requestRepository.streamByBanner(banner)

        writer.printRecord("Banner", "Nr. Cliente", "Cliente", "Tipo de Pedido", "Custo")
        println(file)

        return bannerRequests.onItem().transform { request ->
            writer.printRecord(request.client.banner.name, request.client.id, request.client.name, getType(request.type), request.cost)
            request
        }.collect().asList().onItemOrFailure().transform { _ , e ->
            writer.close(true)

            if (e != null) {
                throw e
            }

            return@transform file
        }
    }

    /**
     * Create a RequestType instance
     */
    private fun toRequestType(request: RequestTypeDto): Uni<out RequestType> {
        return when (request) {
            is OneDto -> toOneFace(request)
            is TwoDto -> toTwoFaces(request)
            is LeftDto -> toLeftShowcase(request)
            is RightDto -> toRightShowcase(request)
            else -> throw InvalidRequestTypeException()
        }
    }

    /**
     * Create a OneFace instance
     */
    private fun toOneFace(request: OneDto): Uni<OneFace> {
        return doSlot(request.cover).onItem().transform { slot ->
            val type = OneFace()
            type.cover = slot
            type
        }
    }

    /**
     * Create a TwoFace instance
     */
    private fun toTwoFaces(request: TwoDto): Uni<TwoFaces> {
        val coverSlot = doSlot(request.cover)
        val backSlot = doSlot(request.back)

        return Uni.join().all(coverSlot, backSlot).andFailFast().onItem().transform { (cover, back) ->
            val type = TwoFaces()
            type.cover = cover
            type.back = back
            type
        }
    }

    /**
     * Create a showcase instance
     */
    private fun toShowcase(request: ShowcaseDto, output: Showcase): Uni<Showcase> {
        val topSlot = doSlot(request.top)
        val bottomSlot = doSlot(request.bottom)
        val leftSlot = doSlot(request.left)
        val rightSlot = doSlot(request.right)
        val sideSlot = doSlot(request.side)

        return Uni.join().all(topSlot, bottomSlot, leftSlot, rightSlot, sideSlot).andFailFast().onItem()
            .transform { (top, bottom, left, right, side) ->
                output.top = top
                output.bottom = bottom
                output.left = left
                output.right = right
                output.side = side

                output
            }
    }

    /**
     * Create a LeftShowcase instance
     */
    private fun toLeftShowcase(request: LeftDto): Uni<Showcase> {
        val showcase = LeftShowcase()

        return toShowcase(request, showcase)
    }

    /**
     * Create a RightShowcase instance
     */
    private fun toRightShowcase(request: RightDto): Uni<Showcase> {
        val showcase = RightShowcase()

        return toShowcase(request, showcase)
    }

    /**
     * Create a request slot
     */
    private fun doSlot(slotDto: RequestSlotDto): Uni<RequestSlot> {
        val materialUni = materialRepository.findById(slotDto.material!!.id)
        val brandUni = brandRepository.findByIdWithImages(slotDto.brand!!.id!!)

        return Uni.combine().all().unis(materialUni, brandUni).asTuple().onItem().transformToUni { tuple ->
            val material = tuple.item1
            val brand = tuple.item2

            if (material == null) {
                throw InvalidIdException("material")
            } else if (brand == null) {
                throw InvalidIdException("brand")
            }

            val image = brand.images.stream().filter { it.id == slotDto.image!!.id }.findFirst().orElse(null)
                ?: throw InvalidIdException("image")

            val slot = RequestSlot()
            slot.image = image
            slot.material = material
            slot.brand = brand
            slot.measurements = slotDto.measurements!!

            requestSlotRepository.persist(slot)
        }
    }

    /**
     * Get request type
     */
    private fun getType(request: RequestTypeDto): String {
        return when (request) {
            is OneDto -> "Uma face"
            is TwoDto -> "Duas faces"
            is ShowcaseDto -> "Montra"
            else -> throw InvalidRequestTypeException()
        }
    }

    private fun getType(request: RequestType): String {
        return when (request) {
            is OneFace -> "Uma face"
            is TwoFaces -> "Duas faces"
            is Showcase -> "Montra"
            else -> throw InvalidRequestTypeException()
        }
    }

    /**
     * Validate the request type
     */
    private fun validateType(request: RequestTypeDto) {
        return when (request) {
            is OneDto -> {
                validateSlot(request.cover)
            }

            is TwoDto -> {
                validateSlot(request.cover)
                validateSlot(request.back)
            }

            is ShowcaseDto -> {
                validateSlot(request.top)
                validateSlot(request.bottom)
                validateSlot(request.left)
                validateSlot(request.right)
                validateSlot(request.side)
            }

            else -> throw InvalidRequestTypeException()
        }
    }

    private fun validateSlot(slot: RequestSlotDto?) {
        validateBrand(slot?.brand)
        validateImage(slot?.image)
        validateMaterial(slot?.material)
        validateMeasurements(slot?.measurements)
    }

    private fun validateBrand(brand: BrandDto?) {
        if (brand?.id == null || brand.id!! <= 0) {
            throw InvalidIdException("brand")
        }
    }

    private fun validateImage(image: ImageDto?) {
        if (image?.id == null || image.id!! <= 0) {
            throw InvalidIdException("image")
        }
    }

    private fun validateMaterial(material: MaterialDto?) {
        if (material?.id == null || material.id!! <= 0) {
            throw InvalidIdException("material")
        }
    }

    private fun validateMeasurements(measurements: Measurements?) {
        if (measurements == null || measurements.height <= 0 || measurements.width <= 0) {
            throw InvalidMeasurementsException()
        }
    }
}
