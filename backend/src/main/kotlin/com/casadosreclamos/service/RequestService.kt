package com.casadosreclamos.service

import com.casadosreclamos.dto.*
import com.casadosreclamos.exception.*
import com.casadosreclamos.getEmailNewStandardRequest
import com.casadosreclamos.model.Request
import com.casadosreclamos.model.Role
import com.casadosreclamos.model.User
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
import java.util.stream.Collectors
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
    lateinit var priceService: PriceService

    @Inject
    lateinit var finishingService: FinishingService

    @Inject
    lateinit var plafondRepository: PlafondRepository

    @Inject
    lateinit var mailer: ReactiveMailer

    @Inject
    @ConfigProperty(name = "cdr.emails")
    lateinit var cdrMails: List<String>

    fun getAll(): Multi<Request> {
        return requestRepository.streamAll()
    }

    @Throws(InvalidIdException::class, InvalidMeasurementsException::class, InvalidRequestTypeException::class)
    fun add(requestDto: NewRequestDto, email: String): Uni<Request> {
        validateRequestDto(requestDto)

        val request = Request()
        request.status = RequestStatus.ORDERED
        request.created = Date()
        request.lastUpdate = request.created
        request.observations = requestDto.observations
        request.application = requestDto.application!!
        request.amount = requestDto.amount!!

        lateinit var clientName: String
        lateinit var user: User
        lateinit var brand: Brand
        lateinit var requestType: RequestType

        val clientUni = clientRepository.findById(requestDto.clientId!!)
        val userUni = userRepository.findByEmail(email)
        val brandUni = brandRepository.findByIdWithImages(requestDto.brand!!.id!!)

        return Panache.withTransaction {
            Uni.combine().all().unis(clientUni, userUni, brandUni).asTuple().onItem().transformToUni { tuple ->
                val client = tuple.item1 ?: throw InvalidIdException("client")
                user = tuple.item2 ?: throw InvalidUserException()
                brand = tuple.item3 ?: throw InvalidIdException("brand")

                clientName = client.name

                request.requester = user
                request.client = client
                request.brand = brand

                // Create RequestType instance
                toRequestType(requestDto.type!!, request.brand.images)
            }.onItem().transformToUni { type ->
                // Assign requestType to request and search for user plafond
                requestType = type

                plafondRepository.findById(user, brand)
            }.onItem().transformToUni { plafond ->
                // Check if user has enough plafond and debit the amount for current request
                // Save the request
                if (plafond == null) {
                    throw NotEnoughCreditsException()
                }

                if (plafond.amount < requestType.cost) {
                    throw NotEnoughCreditsException()
                } else {
                    plafond.amount -= requestType.cost
                }

                requestRepository.persistAndFlush(request)
            }.onItem().transformToUni { _ ->
                // Update the requestType and assign the cost to the request
                requestType.request = request
                request.type = requestType
                request.cost = requestType.cost * request.amount

                requestTypeRepository.persist(requestType)
            }
        }.onItem().transformToUni { _ ->
            // Send email
            mailer.send(
                Mail.withText(
                    "",
                    "Novo pedido efetuado por comercial da Safilo",
                    getEmailNewStandardRequest(user.name, clientName, getType(requestDto.type!!), brand.name)
                ).setTo(cdrMails)
            )
        }.onItem().transform {
            request
        }
    }

    fun getPrice(request: NewRequestDto): Uni<Double> {
        validateRequestDto(request)

        val clientUni = clientRepository.findById(request.clientId!!)
        val brandUni = brandRepository.findByIdWithImages(request.brand!!.id!!)

        return Panache.withTransaction {
            Uni.combine().all().unis(clientUni, brandUni).asTuple().onItem().transformToUni { tuple ->
                tuple.item1 ?: throw InvalidIdException("client")
                val brand = tuple.item2 ?: throw InvalidIdException("brand")

                // Create RequestType instance
                toRequestType(request.type!!, brand.images)
            }.onItem().transform { requestType ->
                requestType.cost * request.amount!!
            }
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

    fun cancel(requestId: Long, email: String): Uni<Response> {
        lateinit var roles: Set<Role>

        // TODO: Send mail

        return Panache.withTransaction {
            userRepository.findByEmail(email).onItem().transformToUni { user ->
                roles = user.roles

                requestRepository.findById(requestId).onItem().ifNotNull().transform { request ->
                    // Prevent other commercials from cancelling arbitrary requests
                    if (roles.contains(Role.ADMIN) || roles.contains(Role.CDR) || roles.contains(Role.MANAGER) || (roles.contains(
                            Role.COMMERCIAL
                        ) && request.requester.name == email)
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

        return bannerRequests.onItem().transform { request ->
            writer.printRecord(
                request.client.banner.name, request.client.id, request.client.name, getType(request.type), request.cost
            )
            request
        }.collect().asList().onItemOrFailure().transform { _, e ->
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
    private fun toRequestType(request: RequestTypeDto, images: List<Image>): Uni<out RequestType> {
        return when (request) {
            is OneDto -> toOneFace(request, images)
            is TwoDto -> toTwoFaces(request, images)
            is LeftDto -> toLeftShowcase(request, images)
            is RightDto -> toRightShowcase(request, images)
            else -> throw InvalidRequestTypeException()
        }
    }

    /**
     * Create a OneFace instance
     */
    private fun toOneFace(request: OneDto, images: List<Image>): Uni<OneFace> {
        return doSlot(request.cover, images).onItem().transform { slot ->
            val type = OneFace()
            type.cover = slot
            type.cost = slot.cost
            type
        }
    }

    /**
     * Create a TwoFace instance
     */
    private fun toTwoFaces(request: TwoDto, images: List<Image>): Uni<TwoFaces> {
        val coverSlot = doSlot(request.cover, images)
        val backSlot = doSlot(request.back, images)

        return Uni.join().all(coverSlot, backSlot).andFailFast().onItem().transform { (cover, back) ->
            val type = TwoFaces()
            type.cover = cover
            type.back = back
            type.cost = cover.cost + back.cost

            type
        }
    }

    /**
     * Create a showcase instance
     */
    private fun toShowcase(request: ShowcaseDto, output: Showcase, images: List<Image>): Uni<Showcase> {
        val topSlot = doSlot(request.top, images)
        val bottomSlot = doSlot(request.bottom, images)
        val leftSlot = doSlot(request.left, images)
        val rightSlot = doSlot(request.right, images)
        val sideSlot = doSlot(request.side, images)

        return Uni.join().all(topSlot, bottomSlot, leftSlot, rightSlot, sideSlot).andFailFast().onItem()
            .transform { (top, bottom, left, right, side) ->
                output.top = top
                output.bottom = bottom
                output.left = left
                output.right = right
                output.side = side

                output.cost = top.cost + bottom.cost + left.cost + right.cost + side.cost

                output
            }
    }

    /**
     * Create a LeftShowcase instance
     */
    private fun toLeftShowcase(request: LeftDto, images: List<Image>): Uni<Showcase> {
        val showcase = LeftShowcase()

        return toShowcase(request, showcase, images)
    }

    /**
     * Create a RightShowcase instance
     */
    private fun toRightShowcase(request: RightDto, images: List<Image>): Uni<Showcase> {
        val showcase = RightShowcase()

        return toShowcase(request, showcase, images)
    }

    /**
     * Create a request slot
     */
    private fun doSlot(slotDto: RequestSlotDto, images: List<Image>): Uni<RequestSlot> {
        val materialUni = materialRepository.findById(slotDto.material!!.id!!)
        val priceUni = priceService.get(slotDto.material!!.id!!, slotDto.finishings!!)
        val finishingUni = if (slotDto.finishings.isNullOrEmpty()) {
            Uni.createFrom().item(setOf())
        } else {
            finishingService.find(slotDto.finishings!!).collect().with(Collectors.toSet())
        }

        return Uni.combine().all().unis(materialUni, priceUni, finishingUni).asTuple().onItem()
            .transformToUni { tuple ->
                val material = tuple.item1
                val price = tuple.item2
                val finishings = tuple.item3

                if (material == null) {
                    throw InvalidIdException("material")
                } else if ((slotDto.finishings != null && slotDto.finishings!!.isNotEmpty() && finishings.isNullOrEmpty()) || price == null) {
                    throw InvalidIdException("finishing")
                }

                // Check if image exists
                val image = images.stream().filter { it.id == slotDto.image!!.id }.findFirst().orElse(null)
                    ?: throw InvalidIdException("image")

                // Check if mandatory finishings are fullfilled
                val tmpFinishings = finishings.toMutableSet()

                for (group in material.mandatoryFinishings) {
                    var changed = false

                    for (mandatory in group.finishings) {
                        if (mandatory in tmpFinishings) {
                            changed = true
                            tmpFinishings.remove(mandatory)
                            break
                        }
                    }

                    if (!changed) {
                        throw MandatoryFinishingMissingException()
                    }
                }

                // Create the request
                val slot = RequestSlot()
                slot.image = image
                slot.material = material
                slot.measurements = slotDto.measurements!!
                slot.finishings = finishings

                // Price per square-meter X Height X Width X Conversion from square-centimeter to square-meter + Finishing
                slot.cost =
                    price.costPerSquareMeter * slot.measurements.height * slot.measurements.width * 0.0001 + price.fixedCost

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
        validateImage(slot?.image)
        validateMaterial(slot?.material)
        validateMeasurements(slot?.measurements)
        validateFinishings(slot?.finishings)
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

    private fun validateFinishings(finishings: Set<FinishingDto>?) {
        if (finishings != null && finishings.stream().filter { it?.id == null || it.id!! <= 0 }.count() > 0) {
            throw InvalidIdException("finishing")
        }
    }

    private fun validateRequestDto(request: NewRequestDto) {
        if (request.clientId == null || request.clientId!! <= 0) {
            throw InvalidIdException("client")
        } else if (request.amount == null || request.amount!! <= 0) {
            throw InvalidAmountException()
        } else if (request.type == null) {
            throw InvalidRequestTypeException()
        } else if (request.application == null) {
            request.application = false
        } else if (request.brand == null || request.brand!!.id!! <= 0) {
            throw InvalidIdException("brand")
        }

        validateType(request.type!!)
    }
}
