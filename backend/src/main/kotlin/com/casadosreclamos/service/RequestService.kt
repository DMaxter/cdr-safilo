package com.casadosreclamos.service

import com.casadosreclamos.dto.*
import com.casadosreclamos.dto.LeftShowcase as LeftDto
import com.casadosreclamos.dto.OneFace as OneDto
import com.casadosreclamos.dto.RightShowcase as RightDto
import com.casadosreclamos.dto.SimpleShowcase as SimpleDto
import com.casadosreclamos.dto.TwoFaces as TwoDto
import com.casadosreclamos.exception.*
import com.casadosreclamos.model.Request
import com.casadosreclamos.model.Role
import com.casadosreclamos.model.User
import com.casadosreclamos.model.request.*
import com.casadosreclamos.repo.*
import com.casadosreclamos.utils.getEmailCancelledRequest
import com.casadosreclamos.utils.getEmailEditedRequest
import com.casadosreclamos.utils.getEmailNewStandardRequest
import io.quarkus.hibernate.reactive.panache.Panache
import io.quarkus.mailer.Mail
import io.quarkus.mailer.reactive.ReactiveMailer
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import java.io.File
import java.io.FileWriter
import java.util.*
import java.util.stream.Collectors
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.core.Response
import javax.ws.rs.core.Response.Status
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.jboss.logging.Logger

private const val SEND_COST = 16.75
private const val APPLICATION_COST = 100

@ApplicationScoped
class RequestService {
    @Inject lateinit var logger: Logger

    @Inject lateinit var requestRepository: RequestRepository

    @Inject lateinit var clientRepository: ClientRepository

    @Inject lateinit var brandRepository: BrandRepository

    @Inject lateinit var materialRepository: MaterialRepository

    @Inject lateinit var requestSlotRepository: RequestSlotRepository

    @Inject lateinit var requestTypeRepository: RequestTypeRepository

    @Inject lateinit var userRepository: UserRepository

    @Inject lateinit var priceService: PriceService

    @Inject lateinit var finishingService: FinishingService

    @Inject lateinit var plafondRepository: PlafondRepository

    @Inject lateinit var mailer: ReactiveMailer

    @Inject @ConfigProperty(name = "cdr.emails") lateinit var cdrMails: List<String>

    @Inject @ConfigProperty(name = "cdr.cancel-emails") lateinit var cancelMails: List<String>

    fun get(id: Long): Uni<Request> {
        return requestRepository.findById(id)
    }

    fun getAll(): Multi<Request> {
        return requestRepository.streamAll()
    }

    @Throws(
            InvalidIdException::class,
            InvalidMeasurementsException::class,
            InvalidRequestTypeException::class
    )
    fun add(requestDto: NewRequestDto, email: String): Uni<Request> {
        logger.info("Creating request $requestDto")

        validateRequestDto(requestDto)

        val request = Request()
        request.status = RequestStatus.ORDERED
        request.created = Date()
        request.lastUpdate = request.created
        request.observations = requestDto.observations
        request.application = requestDto.application!!
        request.amount = requestDto.amount!!

        lateinit var clientName: String
        var user: User? = null
        var brand: Brand? = null
        lateinit var requestType: RequestType

        val clientUni = clientRepository.findById(requestDto.clientId!!)
        val userUni = userRepository.findByEmail(email)
        val brandUni = brandRepository.findByIdWithImages(requestDto.brand!!.id!!)

        return Panache.withTransaction {
            Uni.combine()
                    .all()
                    .unis(clientUni, userUni, brandUni)
                    .asTuple()
                    .onItem()
                    .transformToUni { tuple ->
                        val client = tuple.item1
                        user = tuple.item2
                        brand = tuple.item3

                        if (client == null) {
                            logger.error("Client with ID ${requestDto.clientId} is not registered")

                            throw InvalidIdException("cliente")
                        } else if (user == null) {
                            logger.error("User with ID ${email} is not registered")

                            throw InvalidUserException()
                        } else if (brand == null) {
                            logger.error(
                                    "Brand with ID ${requestDto.brand!!.id!!} is not registered"
                            )

                            throw InvalidIdException("marca")
                        }

                        clientName = client.name

                        request.requester = user!!
                        request.client = client
                        request.brand = brand!!

                        // Create RequestType instance
                        toRequestType(requestDto.type!!, request.brand.images)
                    }
                    .onItem()
                    .transformToUni { type ->
                        // Assign requestType to request and search for user plafond
                        requestType = type

                        plafondRepository.findById(user!!, brand!!)
                    }
                    .onItem()
                    .transformToUni { plafond ->
                        // Check if user has enough plafond and debit the amount for current request
                        if (plafond == null) {
                            logger.error("User doesn't have credits")

                            throw NotEnoughCreditsException()
                        }

                        requestType.cost = requestType.cost * request.amount + SEND_COST

                        if (request.application) {
                            requestType.cost += APPLICATION_COST
                        }

                        if (plafond.amount < requestType.cost) {
                            logger.error(
                                    "User doesn't have enough credits: ${plafond.amount} for request of ${requestType.cost}"
                            )

                            throw NotEnoughCreditsException()
                        } else {
                            plafond.amount -= requestType.cost
                        }

                        logger.info("Successfully debited credits from user")

                        requestRepository
                                .persistAndFlush(request)
                                .onItem()
                                .invoke { _ -> logger.info("Successfully created request") }
                                .onFailure()
                                .invoke { e -> logger.error("Couldn't register request: $e") }
                    }
                    .onItem()
                    .transformToUni { _ ->
                        // Update the requestType and assign the cost to the request
                        requestType.request = request
                        request.type = requestType
                        request.cost = requestType.cost

                        requestTypeRepository
                                .persist(requestType)
                                .onItem()
                                .invoke { _ -> logger.info("Successfully created requestType") }
                                .onFailure()
                                .invoke { e -> logger.error("Couldn't register requestType: $e") }
                    }
        }
                .onItem()
                .transformToUni { _ ->
                    // Send email
                    mailer.send(
                                    Mail.withText(
                                                    "",
                                                    "Novo pedido efetuado por comercial da Safilo",
                                                    getEmailNewStandardRequest(
                                                            user!!.name,
                                                            clientName,
                                                            getType(requestDto.type!!),
                                                            brand!!.name
                                                    )
                                            )
                                            .setTo(cdrMails)
                            )
                            .onItem()
                            .invoke { _ -> logger.info("Sent email with request") }
                            .onFailure()
                            .invoke { e -> logger.error("Couldn't send email with request: $e") }
                }
                .onItem()
                .transform {
                    logger.info("Successful operation")

                    request
                }
    }

    fun getPrice(request: NewRequestDto): Uni<Double> {
        logger.info("Creating request $request")

        validateRequestDto(request)

        val clientUni = clientRepository.findById(request.clientId!!)
        val brandUni = brandRepository.findByIdWithImages(request.brand!!.id!!)

        return Panache.withTransaction {
            Uni.combine()
                    .all()
                    .unis(clientUni, brandUni)
                    .asTuple()
                    .onItem()
                    .transformToUni { tuple ->
                        val client = tuple.item1
                        val brand = tuple.item2

                        if (client == null) {
                            logger.error("Client with ID ${request.clientId} is not registered")

                            throw InvalidIdException("cliente")
                        } else if (brand == null) {
                            logger.error("Brand with ID ${request.brand!!.id!!} is not registered")

                            throw InvalidIdException("marca")
                        }

                        // Create RequestType instance
                        toRequestType(request.type!!, brand.images)
                    }
                    .onItem()
                    .transform { requestType ->
                        var cost = requestType.cost * request.amount!! + SEND_COST

                        if (request.application!!) {
                            cost += APPLICATION_COST
                        }

                        logger.info("The request cost is $cost")

                        cost
                    }
        }
    }

    fun toProduction(requestId: Long): Uni<Response> {
        return Panache.withTransaction {
            requestRepository
                    .findById(requestId)
                    .onItem()
                    .ifNotNull()
                    .transform { request ->
                        request.status = RequestStatus.IN_PRODUCTION
                        request.lastUpdate = Date()

                        logger.info("Successful change to production")

                        Response.ok().build()
                    }
                    .onItem()
                    .ifNull()
                    .continueWith {
                        logger.error("Request with ID ${requestId} is not registered")

                        Response.status(Status.NOT_FOUND).build()
                    }
        }
    }

    fun finishRequest(requestId: Long, code: Long?): Uni<Response> {
        return Panache.withTransaction {
            requestRepository.findById(requestId).onItem().transform { request ->
                if (request == null) {
                    logger.error("Request with ID ${requestId} is not registered")

                    throw InvalidIdException("pedido")
                }

                request.status = RequestStatus.DONE
                request.lastUpdate = Date()
                request.trackingCode = code

                logger.info("Successful change to finished")

                Response.ok().build()
            }
        }
    }

    fun edit(requestId: Long, email: String, requestDto: NewRequestDto): Uni<Response> {
        logger.info("Editing request $requestId")

        validateRequestDto(requestDto)

        lateinit var roles: Set<Role>

        val clientUni = clientRepository.findById(requestDto.clientId!!)
        val brandUni = brandRepository.findByIdWithImages(requestDto.brand!!.id!!)

        lateinit var editRequest: Request
        lateinit var clientName: String
        var brand: Brand? = null
        lateinit var requestType: RequestType
        lateinit var requester: User

        var previousCost: Double = 0.0

        return Panache.withTransaction {
            userRepository.findByEmail(email).onItem().transformToUni { user ->
                roles = user.roles

                requestRepository
                        .findById(requestId)
                        .onItem()
                        .ifNotNull()
                        .transform { request ->
                            if (request == null) {
                                logger.error("Request with ID ${requestId} is not registered")

                                throw InvalidIdException("pedido")
                            }

                            // Prevent other commercials from editing arbitrary requests
                            if (roles.contains(Role.ADMIN) ||
                                            roles.contains(Role.CDR) ||
                                            roles.contains(Role.MANAGER) ||
                                            (roles.contains(Role.COMMERCIAL) &&
                                                    request.requester.email == email)
                            ) {
                                request.lastUpdate = Date()
                                request.observations = requestDto.observations
                                request.application = requestDto.application!!
                                request.amount = requestDto.amount!!

                                requester = request.requester

                                editRequest = request
                            } else {
                                logger.error("Commercial doesn't have permission to delete request")

                                throw OperationNotPerformedException()
                            }

                            request
                        }
                        .onItem()
                        .transformToUni { request ->
                            Uni.combine()
                                    .all()
                                    .unis(clientUni, brandUni)
                                    .asTuple()
                                    .onItem()
                                    .transformToUni { tuple ->
                                        val client = tuple.item1
                                        brand = tuple.item2

                                        if (client == null) {
                                            logger.error(
                                                    "Client with ID ${requestDto.clientId} is not registered"
                                            )

                                            throw InvalidIdException("cliente")
                                        } else if (user == null) {
                                            logger.error("User with ID ${email} is not registered")

                                            throw InvalidUserException()
                                        } else if (brand == null) {
                                            logger.error(
                                                    "Brand with ID ${requestDto.brand!!.id!!} is not registered"
                                            )

                                            throw InvalidIdException("marca")
                                        }

                                        clientName = client.name

                                        request.client = client
                                        request.brand = brand!!

                                        previousCost = request.cost!!

                                        // Delete previous RequestType
                                        request.type = null
                                        requestTypeRepository
                                                .deleteById(requestId)
                                                .onItem()
                                                .transformToUni { _ ->
                                                    // Create RequestType instance
                                                    toRequestType(
                                                            requestDto.type!!,
                                                            request.brand.images
                                                    )
                                                }
                                    }
                        }
                        .onItem()
                        .transformToUni { type ->
                            // Assign requestType to request and search for user plafond
                            requestType = type

                            plafondRepository.findById(requester, brand!!)
                        }
                        .onItem()
                        .transformToUni { plafond ->
                            // Check if user has enough plafond and debit the amount for current
                            // request
                            if (plafond == null) {
                                logger.error("User doesn't have credits")

                                throw NotEnoughCreditsException()
                            }

                            requestType.cost = requestType.cost * editRequest.amount + SEND_COST
                            if (requestDto.application!!) {
                                requestType.cost += APPLICATION_COST
                            }

                            val difference = requestType.cost - previousCost

                            if (plafond.amount < difference) {
                                logger.error(
                                        "User doesn't have enough credits: ${plafond.amount} to edit request of ${requestType.cost}"
                                )

                                throw NotEnoughCreditsException()
                            } else {
                                plafond.amount -= difference
                            }

                            logger.info("Successfully debited credits from user")

                            requestRepository.flush()
                        }
                        .onItem()
                        .transformToUni { _ ->
                            // Update the requestType and assign the cost to the request
                            requestType.request = editRequest
                            editRequest.type = requestType
                            editRequest.cost = requestType.cost

                            requestTypeRepository
                                    .persist(requestType)
                                    .onItem()
                                    .invoke { _ -> logger.info("Successfully created requestType") }
                                    .onFailure()
                                    .invoke { e ->
                                        logger.error("Couldn't register requestType: $e")
                                    }
                        }
                        .onItem()
                        .transformToUni { _ ->
                            // Send email
                            mailer.send(
                                            Mail.withText(
                                                            "",
                                                            "Pedido da Safilo editado",
                                                            getEmailEditedRequest(
                                                                    requestId,
                                                                    requester.name,
                                                                    clientName,
                                                                    getType(requestType),
                                                                    brand!!.name
                                                            )
                                                    )
                                                    .setTo(cdrMails)
                                    )
                                    .onItem()
                                    .invoke { _ -> logger.info("Sent email with request") }
                                    .onFailure()
                                    .invoke { e ->
                                        logger.error("Couldn't send email with request: $e")
                                    }
                        }
                        .onItem()
                        .transform { _ ->
                            logger.info("Successful modification of request")

                            Response.ok().build()
                        }
            }
        }
    }

    fun cancel(requestId: Long, email: String): Uni<Response> {
        lateinit var roles: Set<Role>
        lateinit var request: Request

        return Panache.withTransaction {
            userRepository.findByEmail(email).onItem().transformToUni { user ->
                roles = user.roles

                requestRepository
                        .findById(requestId)
                        .onItem()
                        .ifNotNull()
                        .transformToUni { req ->
                            if (req == null) {
                                logger.error("Request with ID ${requestId} is not registered")

                                throw InvalidIdException("pedido")
                            }

                            request = req

                            // Prevent other commercials from cancelling arbitrary requests
                            if (roles.contains(Role.ADMIN) ||
                                            roles.contains(Role.CDR) ||
                                            roles.contains(Role.MANAGER) ||
                                            (roles.contains(Role.COMMERCIAL) &&
                                                    request.requester.email == email)
                            ) {
                                request.status = RequestStatus.CANCELLED
                                request.lastUpdate = Date()
                            } else {
                                logger.error("Commercial doesn't have permission to delete request")

                                throw OperationNotPerformedException()
                            }

                            plafondRepository.findById(request.requester, request.brand)
                        }
                        .onItem()
                        .transformToUni { plafond ->
                            plafond!!.amount += request.cost!!

                            // Send email
                            mailer.send(
                                            Mail.withText(
                                                            "",
                                                            "Pedido da Safilo cancelado",
                                                            getEmailCancelledRequest(
                                                                    user!!.name,
                                                                    request.client.name,
                                                                    requestId,
                                                                    getType(request.type!!)
                                                            )
                                                    )
                                                    .setTo(cancelMails)
                                    )
                                    .onItem()
                                    .invoke { _ -> logger.info("Sent email with request") }
                                    .onFailure()
                                    .invoke { e ->
                                        logger.error("Couldn't send email with request: $e")
                                    }
                        }
                        .onItem()
                        .transform { _ ->
                            logger.info("Successful cancellation of request")

                            Response.ok().build()
                        }
            }
        }
    }

    fun exportBanner(): Uni<File> {
        val file = kotlin.io.path.createTempFile().toFile()
        val writer = CSVPrinter(FileWriter(file), CSVFormat.EXCEL)

        val bannerRequests =
                requestRepository.streamAll().onCompletion().invoke { ->
                    logger.info("Fetched all requests")
                }

        writer.printRecord(
                "Banner",
                "Nr. Cliente",
                "Cliente",
                "Comercial",
                "Nr. Pedido",
                "Estado",
                "Data",
                "Tipo de Pedido",
                "Aplicação",
                "Materiais",
                "Marca",
                "Custo"
        )

        return bannerRequests
                .onItem()
                .transform { request ->
                    writer.printRecord(
                            request.client.banner?.name,
                            request.client.id,
                            request.client.name,
                            request.requester.email,
                            request.id,
                            getStatus(request.status),
                            request.created.toString(),
                            getType(request.type!!),
                            if (request.application) {
                                "Sim"
                            } else {
                                "Não"
                            },
                            getMaterials(request.type!!),
                            request.brand.name,
                            request.cost
                    )
                    request
                }
                .collect()
                .asList()
                .onItem()
                .transform { _ ->
                    writer.close(true)

                    logger.info("Successfully exported banner requests")

                    return@transform file
                }
                .onFailure()
                .invoke { e -> logger.error("Couldn't export banner requests: $e") }
    }

    /** Create a RequestType instance */
    private fun toRequestType(request: RequestTypeDto, images: List<Image>): Uni<out RequestType> {
        return when (request) {
            is OneDto -> toOneFace(request, images)
            is TwoDto -> toTwoFaces(request, images)
            is SimpleDto -> toSimpleShowcase(request, images)
            is LeftDto -> toLeftShowcase(request, images)
            is RightDto -> toRightShowcase(request, images)
            else -> {
                logger.error("Invalid request type")

                throw InvalidRequestTypeException()
            }
        }
    }

    /** Create a OneFace instance */
    private fun toOneFace(request: OneDto, images: List<Image>): Uni<OneFace> {
        return doSlot(request.cover, images).onItem().transform { slot ->
            val type = OneFace()
            type.cover = slot
            type.cost = slot.cost
            type
        }
    }

    /** Create a TwoFace instance */
    private fun toTwoFaces(request: TwoDto, images: List<Image>): Uni<TwoFaces> {
        val coverSlot = doSlot(request.cover, images)
        val backSlot = doSlot(request.back, images)

        return Uni.join().all(coverSlot, backSlot).andFailFast().onItem().transform { (cover, back)
            ->
            val type = TwoFaces()
            type.cover = cover
            type.back = back
            type.cost = cover.cost + back.cost

            type
        }
    }

    private fun toSimpleShowcase(request: SimpleDto, images: List<Image>): Uni<SimpleShowcase> {
        val topSlot = doSlot(request.top, images)
        val bottomSlot = doSlot(request.bottom, images)
        val leftSlot = doSlot(request.left, images)
        val rightSlot = doSlot(request.right, images)

        return Uni.join()
                .all(topSlot, bottomSlot, leftSlot, rightSlot)
                .andFailFast()
                .onItem()
                .transform { (top, bottom, left, right) ->
                    val type = SimpleShowcase()

                    type.top = top
                    type.bottom = bottom
                    type.left = left
                    type.right = right

                    type.cost = top.cost + bottom.cost + left.cost + right.cost

                    type
                }
    }

    /** Create a showcase instance */
    private fun toShowcase(
            request: ShowcaseDto,
            output: Showcase,
            images: List<Image>
    ): Uni<Showcase> {
        val topSlot = doSlot(request.top, images)
        val bottomSlot = doSlot(request.bottom, images)
        val leftSlot = doSlot(request.left, images)
        val rightSlot = doSlot(request.right, images)
        val sideSlot = doSlot(request.side, images)

        return Uni.join()
                .all(topSlot, bottomSlot, leftSlot, rightSlot, sideSlot)
                .andFailFast()
                .onItem()
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

    /** Create a LeftShowcase instance */
    private fun toLeftShowcase(request: LeftDto, images: List<Image>): Uni<Showcase> {
        val showcase = LeftShowcase()

        return toShowcase(request, showcase, images)
    }

    /** Create a RightShowcase instance */
    private fun toRightShowcase(request: RightDto, images: List<Image>): Uni<Showcase> {
        val showcase = RightShowcase()

        return toShowcase(request, showcase, images)
    }

    /** Create a request slot */
    private fun doSlot(slotDto: RequestSlotDto, images: List<Image>): Uni<RequestSlot> {
        val materialUni = materialRepository.findById(slotDto.material!!.id!!)
        val priceUni = priceService.get(slotDto.material!!.id!!, slotDto.finishings!!)
        val finishingUni =
                if (slotDto.finishings.isNullOrEmpty()) {
                    Uni.createFrom().item(setOf())
                } else {
                    finishingService
                            .find(slotDto.finishings!!)
                            .filter { finishing -> !finishing.obsolete }
                            .collect()
                            .with(Collectors.toSet())
                }

        return Uni.combine()
                .all()
                .unis(materialUni, priceUni, finishingUni)
                .asTuple()
                .onItem()
                .transformToUni { tuple ->
                    val material = tuple.item1
                    val price = tuple.item2
                    val finishings = tuple.item3

                    if (material == null) {
                        logger.error(
                                "Material with ID ${slotDto.material!!.id!!} is not registered"
                        )

                        throw InvalidIdException("material")
                    } else if ((slotDto.finishings != null &&
                                    slotDto.finishings!!.isNotEmpty() &&
                                    finishings.isNullOrEmpty()) || price == null
                    ) {
                        logger.error(
                                "Invalid or repeated finishings detected. Fetched: $finishings"
                        )

                        throw InvalidIdException("acabamento")
                    }

                    // Check if image exists
                    val image =
                            images.stream()
                                    .filter { it.id == slotDto.image!!.id }
                                    .findFirst()
                                    .orElse(null)

                    if (image == null) {
                        logger.error("Image with ID ${slotDto.image!!.id} is not registered")

                        throw InvalidIdException("imagem")
                    }

                    // Check if mandatory finishings are fulfilled
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
                            logger.error("Not all mandatory finishings were fullfilled")

                            throw MandatoryFinishingMissingException()
                        }
                    }

                    // Create the request
                    val slot = RequestSlot()
                    slot.image = image
                    slot.material = material
                    slot.measurements = slotDto.measurements!!
                    slot.finishings = finishings

                    // Price per square-meter X Height X Width X Conversion from square-centimeter
                    // to square-meter + Finishing
                    slot.cost =
                            price.costPerSquareMeter *
                                    slot.measurements.height *
                                    slot.measurements.width *
                                    0.0001 + price.fixedCost

                    requestSlotRepository
                            .persist(slot)
                            .onItem()
                            .invoke { _ -> logger.info("Successfully created slot") }
                            .onFailure()
                            .invoke { e -> logger.error("Couldn't register slot: $e") }
                }
    }

    /** Get request type */
    private fun getType(request: RequestTypeDto): String {
        return when (request) {
            is OneDto -> "Uma face"
            is TwoDto -> "Duas faces"
            is ShowcaseDto -> "Montra"
            is SimpleDto -> "Montra simples"
            else -> throw InvalidRequestTypeException()
        }
    }

    private fun getType(request: RequestType): String {
        return when (request) {
            is OneFace -> "Uma face"
            is TwoFaces -> "Duas faces"
            is Showcase -> "Montra"
            is SimpleShowcase -> "Montra simples"
            else -> throw InvalidRequestTypeException()
        }
    }

    private fun getMaterials(request: RequestType): String {
        return when (request) {
            is OneFace -> request.cover.material.name
            is TwoFaces -> request.cover.material.name + " | " + request.back.material.name
            is Showcase ->
                    request.top.material.name +
                            " | " +
                            request.right.material.name +
                            " | " +
                            request.bottom.material.name +
                            " | " +
                            request.left.material.name +
                            " | " +
                            request.side.material.name
            is SimpleShowcase ->
                    request.top.material.name +
                            " | " +
                            request.right.material.name +
                            " | " +
                            request.bottom.material.name +
                            " | " +
                            request.left.material.name
            else -> throw InvalidRequestTypeException()
        }
    }

    private fun getStatus(status: RequestStatus): String {
        return when (status) {
            RequestStatus.ORDERED -> "Encomendado"
            RequestStatus.CANCELLED -> "Cancelado"
            RequestStatus.DONE -> "Terminado"
            RequestStatus.IN_PRODUCTION -> "Em produção"
        }
    }

    /** Validate the request type */
    private fun validateType(request: RequestTypeDto) {
        return when (request) {
            is OneDto -> {
                validateSlot(request.cover)
            }
            is TwoDto -> {
                validateSlot(request.cover)
                validateSlot(request.back)
            }
            is SimpleDto -> {
                validateSlot(request.top)
                validateSlot(request.bottom)
                validateSlot(request.left)
                validateSlot(request.right)
            }
            is ShowcaseDto -> {
                validateSlot(request.top)
                validateSlot(request.bottom)
                validateSlot(request.left)
                validateSlot(request.right)
                validateSlot(request.side)
            }
            else -> {
                logger.error("Request has unknown type $request")

                throw InvalidRequestTypeException()
            }
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
            logger.error("Slot image ID is invalid")

            throw InvalidIdException("imagem")
        }
    }

    private fun validateMaterial(material: MaterialDto?) {
        if (material?.id == null || material.id!! <= 0) {
            logger.error("Slot material ID is invalid")

            throw InvalidIdException("material")
        }
    }

    private fun validateMeasurements(measurements: Measurements?) {
        if (measurements == null || measurements.height <= 0 || measurements.width <= 0) {
            logger.error("Slot measurements are invalid")

            throw InvalidMeasurementsException()
        }
    }

    private fun validateFinishings(finishings: Set<FinishingDto>?) {
        if (finishings != null &&
                        finishings.stream().filter { it?.id == null || it.id!! <= 0 }.count() > 0
        ) {
            logger.error("At least one slot finishing has invalid ID")

            throw InvalidIdException("acabamento")
        }
    }

    private fun validateRequestDto(request: NewRequestDto) {
        if (request.clientId == null || request.clientId!! <= 0) {
            logger.error("Request Client ID is invalid")

            throw InvalidIdException("cliente")
        } else if (request.amount == null || request.amount!! <= 0) {
            logger.error("Request amount is invalid")

            throw InvalidAmountException()
        } else if (request.type == null) {
            logger.error("Request type is invalid")

            throw InvalidRequestTypeException()
        } else if (request.application == null) {
            logger.error("Request application is null")

            request.application = false
        } else if (request.brand == null || request.brand!!.id!! <= 0) {
            logger.error("Request Brand ID is invalid")

            throw InvalidIdException("marca")
        }

        validateType(request.type!!)
    }
}
