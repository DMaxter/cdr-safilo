package com.casadosreclamos.service

import com.casadosreclamos.dto.fema.*
import com.casadosreclamos.exception.InvalidAmountException
import com.casadosreclamos.exception.InvalidIdException
import com.casadosreclamos.exception.InvalidMeasurementsException
import com.casadosreclamos.exception.fema.EmptyDescriptionException
import com.casadosreclamos.exception.fema.InvalidLabelFormatException
import com.casadosreclamos.exception.fema.WaybillAlreadyOpenedException
import com.casadosreclamos.exception.fema.WaybillNotOpenedException
import com.casadosreclamos.utils.FemaClient
import com.casadosreclamos.utils.getEmailDailyReportSuccess
import io.quarkus.hibernate.reactive.panache.Panache
import io.quarkus.mailer.Mail
import io.quarkus.mailer.reactive.ReactiveMailer
import io.quarkus.scheduler.Scheduled
import io.smallrye.mutiny.Uni
import java.io.File
import java.nio.file.Files
import java.nio.file.LinkOption
import java.nio.file.attribute.FileTime
import java.time.Instant
import java.time.ZoneId
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.jboss.logging.Logger

private const val CDR_NAME = "Casa dos Reclamos"
private const val CDR_ADDRESS = "Rua Narciso José Machado Guimarães"
private const val CDR_POSTAL = "4795-089"
private const val CDR_CITY = "Vila das Aves"
private const val CDR_COUNTRY = "PT"
private const val CDR_PHONE = "+351 252 871 364"

private const val LAST_WAYBILL_FILE = "/safilo/last_waybill"

@ApplicationScoped
class WaybillService {
    @Inject lateinit var logger: Logger

    @Inject lateinit var requestService: RequestService

    @Inject lateinit var client: FemaClient

    @Inject lateinit var mailer: ReactiveMailer

    @Inject @ConfigProperty(name = "fema.report.emails") lateinit var emails: List<String>

    fun getShippingServices(requestId: Long): Uni<List<Service>> {
        if (requestId <= 0) {
            logger.error("Request ID is invalid!")
            throw InvalidIdException("pedido")
        }

        logger.info("Retrieving request with id $requestId")

        return requestService.get(requestId).onItem().transformToUni { request ->
            if (request == null) {
                logger.error("Request ID is invalid!")
                throw InvalidIdException("pedido")
            }

            val source = Address(CDR_POSTAL, CDR_COUNTRY, CDR_CITY)

            val destination = Address(request.client.postalCode, "PT", "Cidade Teste")

            logger.info("Retrieving available services from FEMA")
            client.getServices(source, destination)
        }
    }

    fun getPackageTypes(): Uni<List<PackageType>> {
        return client.getPackageTypes()
    }

    fun getLabelFormats(): Uni<List<LabelFormat>> {
        return client.getLabelFormats()
    }

    fun openWaybill(requestId: Long, waybillDto: WaybillDto): Uni<File> {
        if (requestId <= 0) {
            logger.error("Request ID is invalid!")
            throw InvalidIdException("pedido")
        } else if (waybillDto.service?.id.isNullOrBlank()) {
            logger.error("FEMA service is invalid!")
            throw InvalidIdException("serviço da FEMA")
        } else if (waybillDto.packaging == null || waybillDto.packaging.id.isNullOrBlank()) {
            logger.error("FEMA package type is invalid!")
            throw InvalidIdException("tipo de embalagem da FEMA")
        } else if (waybillDto.totalWeight == null || waybillDto.totalWeight <= 0) {
            logger.error("Weight is invalid!")
            throw InvalidAmountException()
        } else if (waybillDto.labelFormat.isNullOrBlank()) {
            logger.error("Label format is invalid!")
            throw InvalidLabelFormatException()
        } else if (waybillDto.items == null || waybillDto.items <= 0) {
            logger.error("Amount of items is invalid!")
            throw InvalidAmountException()
        } else if (waybillDto.description.isNullOrBlank()) {
            logger.error("Description is empty!")
            throw EmptyDescriptionException()
        } else if (waybillDto.dimensions?.length == null ||
                        waybillDto.dimensions.length <= 0 ||
                        waybillDto.dimensions.height == null ||
                        waybillDto.dimensions.height <= 0 ||
                        waybillDto.dimensions.width == null ||
                        waybillDto.dimensions.width <= 0
        ) {
            logger.error("Package dimensions are invalid!")
            throw InvalidMeasurementsException()
        }

        logger.info("Retrieving request with id $requestId")

        return Panache.withTransaction {
            requestService.get(requestId).onItem().transformToUni { request ->
                if (request == null) {
                    logger.error("Request ID is invalid!")
                    throw InvalidIdException("pedido")
                } else if (request.trackingCode != null) {
                    logger.error("Waybill already opened for request!")
                    throw WaybillAlreadyOpenedException()
                }

                val source =
                        Contact(
                                CDR_NAME,
                                Address(CDR_POSTAL, CDR_COUNTRY, CDR_CITY, CDR_ADDRESS),
                                CDR_PHONE
                        )

                waybillDto.reference = requestId.toString()
                waybillDto.source = source

                val destination =
                        Contact(
                                request.client.name,
                                Address(
                                        request.client.postalCode,
                                        request.client.country,
                                        request.client.city,
                                        request.client.address
                                ),
                                request.client.phone
                        )

                // Override default parameters with the ones from request
                if (waybillDto.destination != null) {
                    if (waybillDto.destination!!.address?.postalCode != null) {
                        destination.address!!.postalCode =
                                waybillDto.destination!!.address!!.postalCode
                    }
                    if (waybillDto.destination!!.address?.address != null) {
                        destination.address!!.address = waybillDto.destination!!.address!!.address
                    }
                    if (waybillDto.destination!!.address?.city != null) {
                        destination.address!!.city = waybillDto.destination!!.address?.city
                    }
                    if (waybillDto.destination!!.address?.country != null) {
                        destination.address!!.country = waybillDto.destination!!.address?.country
                    }
                }

                waybillDto.destination = destination

                if (waybillDto.description == null) {
                    waybillDto.description = ""
                }

                logger.info("Opening waybill at FEMA")
                client.openWaybill(waybillDto)
                        .onItem()
                        .transform { label ->
                            request.trackingCode = label.id

                            label.file
                        }
                        .onItem()
                        .transform { file ->
                            val date_file = File(LAST_WAYBILL_FILE)

                            date_file.createNewFile()

                            Files.setLastModifiedTime(
                                    date_file.toPath(),
                                    FileTime.from(Instant.now())
                            )

                            file
                        }
            }
        }
    }

    fun cancelWaybill(requestId: Long): Uni<Void> {
        if (requestId <= 0) {
            logger.error("Request ID is invalid!")
            throw InvalidIdException("pedido")
        }

        logger.info("Retrieving request with id $requestId")

        return Panache.withTransaction {
            requestService.get(requestId).onItem().transformToUni { request ->
                if (request == null) {
                    logger.error("Request ID is invalid!")
                    throw InvalidIdException("pedido")
                } else if (request.trackingCode == null) {
                    logger.error("Waybill not opened for request!")
                    throw WaybillNotOpenedException()
                }

                logger.info("Cancelling waybill at FEMA")
                client.cancelWaybill(request.trackingCode!!).onItem().transform {
                    request.trackingCode = null

                    it
                }
            }
        }
    }

    fun getWaybill(requestId: Long, labelFormat: String): Uni<File> {
        if (requestId <= 0) {
            logger.error("Request ID is invalid!")
            throw InvalidIdException("pedido")
        }

        logger.info("Retrieving request with id $requestId")

        return requestService.get(requestId).onItem().transformToUni { request ->
            if (request == null) {
                logger.error("Request ID is invalid!")
                throw InvalidIdException("pedido")
            } else if (request.trackingCode == null) {
                logger.error("Waybill not opened for request!")
                throw WaybillNotOpenedException()
            } else if (labelFormat.isBlank()) {
                logger.error("Label format is invalid!")
                throw InvalidLabelFormatException()
            }

            logger.info("Getting waybill at FEMA")
            client.getWaybill(request.trackingCode!!, labelFormat)
        }
    }

    @Scheduled(cron = "{fema.report.cron}")
    fun closeDay(): Uni<Void> {
        logger.info("Closing day at FEMA")

        val file = File(LAST_WAYBILL_FILE)

        val modification_date =
                Files.getLastModifiedTime(file.toPath(), LinkOption.NOFOLLOW_LINKS)
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
        val current_date = Instant.now().atZone(ZoneId.systemDefault()).toLocalDate()

        if (!current_date.isEqual(modification_date)) {
            logger.info("No waybills opened today")
            return Uni.createFrom().voidItem()
        }

        return client.closeDay().onItem().transformToUni { report ->
            val currentDate = Calendar.getInstance()

            logger.info("Sending email with report")
            return@transformToUni mailer.send(
                            Mail.withText("", "Fecho diário FEMA", getEmailDailyReportSuccess())
                                    .addAttachment(
                                            "FEMA-Relatório_${currentDate.get(Calendar.YEAR)}-${currentDate.get(Calendar.MONTH)}-${
                        currentDate.get(Calendar.DAY_OF_MONTH)
                    }.pdf",
                                            report.readBytes(),
                                            "application/pdf"
                                    )
                                    .setTo(emails)
                    )
                    .onItem()
                    .invoke { _ -> logger.info("Successfully sent email") }
                    .onFailure()
                    .invoke { e -> logger.error("Couldn't send email: $e") }
        }
    }
}
