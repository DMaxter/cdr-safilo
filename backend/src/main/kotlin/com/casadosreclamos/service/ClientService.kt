package com.casadosreclamos.service

import com.casadosreclamos.utils.EMAIL_REGEX
import com.casadosreclamos.utils.PHONE_REGEX
import com.casadosreclamos.dto.ClientDto
import com.casadosreclamos.exception.*
import com.casadosreclamos.model.Banner
import com.casadosreclamos.model.Client
import com.casadosreclamos.repo.BannerRepository
import com.casadosreclamos.repo.ClientRepository
import io.quarkus.hibernate.reactive.panache.Panache
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.jboss.logging.Logger
import java.io.File
import java.nio.charset.StandardCharsets
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class ClientService {
    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var clientRepository: ClientRepository

    @Inject
    lateinit var bannerRepository: BannerRepository

    fun getAll(): Multi<Client> {
        return clientRepository.streamAll()
    }

    fun get(id: Long): Uni<Client> {
        return clientRepository.findById(id)
    }

    fun getByBanner(banner: String): Multi<Client> {
        return clientRepository.streamByBanner(banner)
    }

    @Throws(
        InvalidIdException::class,
        InvalidBannerException::class,
        InvalidEmailException::class,
        InvalidNameException::class,
        InvalidFiscalNumberException::class,
        InvalidPhoneException::class,
        InvalidAddressException::class,
        InvalidPostalCodeException::class
    )
    fun add(clientDto: ClientDto): Uni<Client> {
        logger.info("Registering client $clientDto")

        val client = Client()

        if (clientDto.id == null || clientDto.id!! <= 0) {
            logger.error("Client ID is invalid")

            throw InvalidIdException("client")
        } else if (clientDto.email == null || clientDto.email!!.isEmpty() || !EMAIL_REGEX.matches(clientDto.email!!)) {
            logger.error("Client email is null, empty or has incorrect format")

            throw InvalidEmailException()
        } else if (clientDto.name == null || clientDto.name!!.isEmpty()) {
            logger.error("Client name is null or empty")

            throw InvalidNameException()
        } else if (clientDto.fiscalNumber == null) {
            logger.error("Client fiscal number is null")

            throw InvalidFiscalNumberException()
        } else if (clientDto.phone == null || clientDto.phone!!.isEmpty() || !PHONE_REGEX.matches(clientDto.phone!!)) {
            logger.error("Client phone number is null, empty or has incorrect format")

            throw InvalidPhoneException()
        } else if (clientDto.address == null || clientDto.address!!.isEmpty()) {
            logger.error("Client address is null or empty")

            throw InvalidAddressException()
        } else if (clientDto.postalCode == null || clientDto.postalCode!!.isEmpty()) {
            logger.error("Client postal code is null or empty")

            throw InvalidPostalCodeException()
        } else if (clientDto.city == null || clientDto.city!!.isEmpty()) {
            logger.error("Client city is null or empty")

            throw InvalidCityException()
        } else if (clientDto.country == null || clientDto.country!!.isEmpty()) {
            logger.error("Client country is null or empty")

            throw InvalidCountryException()
        }

        client.id = clientDto.id!!
        client.email = clientDto.email!!
        client.name = clientDto.name!!
        client.fiscalNumber = clientDto.fiscalNumber!!
        client.phone = clientDto.phone!!
        client.address = clientDto.address!!
        client.postalCode = clientDto.postalCode!!
        client.city = clientDto.city!!
        client.country = clientDto.country!!
        client.images = mutableSetOf()

        val bannerUni = if (clientDto.banner == null) {
            Uni.createFrom().nullItem()
        } else {
            createBannerIfNotExists(clientDto.banner!!)
        }

        return bannerUni.onItem().transformToUni { banner ->
            client.banner = banner

            Panache.withTransaction {
                clientRepository.persist(client).onItem().invoke { _ -> logger.info("Successfully registered client") }
                    .onFailure().invoke { e -> logger.error("Couldn't create client: $e") }
            }
        }
    }

    fun update(clientDto: ClientDto): Uni<Client> {
        logger.info("Updating client $clientDto")

        if (clientDto.id == null || clientDto.id!! <= 0) {
            logger.error("Client ID is invalid")

            throw InvalidIdException("client")
        } else if (clientDto.email == null || clientDto.email!!.isEmpty() || !EMAIL_REGEX.matches(clientDto.email!!)) {
            logger.error("Client email is null, empty or has incorrect format")

            throw InvalidEmailException()
        } else if (clientDto.name == null || clientDto.name!!.isEmpty()) {
            logger.error("Client name is null or empty")

            throw InvalidNameException()
        } else if (clientDto.fiscalNumber == null) {
            logger.error("Client fiscal number is null")

            throw InvalidFiscalNumberException()
        } else if (clientDto.phone == null || clientDto.phone!!.isEmpty() || !PHONE_REGEX.matches(clientDto.phone!!)) {
            logger.error("Client phone number is null, empty or has incorrect format")

            throw InvalidPhoneException()
        } else if (clientDto.address == null || clientDto.address!!.isEmpty()) {
            logger.error("Client address is null or empty")

            throw InvalidAddressException()
        } else if (clientDto.postalCode == null || clientDto.postalCode!!.isEmpty()) {
            logger.error("Client postal code is null or empty")

            throw InvalidPostalCodeException()
        } else if (clientDto.city == null || clientDto.city!!.isEmpty()) {
            logger.error("Client city is null or empty")

            throw InvalidCityException()
        } else if (clientDto.country == null || clientDto.country!!.isEmpty()) {
            logger.error("Client country is null or empty")

            throw InvalidCountryException()
        }

        return Panache.withTransaction {
            clientRepository.findById(clientDto.id).onItem().transform { client ->
                if (client == null) {
                    logger.error("Client with ID ${clientDto.id} is not registered")

                    throw InvalidIdException("client")
                }

                client.email = clientDto.email!!
                client.name = clientDto.name!!
                client.fiscalNumber = clientDto.fiscalNumber!!
                client.phone = clientDto.phone!!
                client.address = clientDto.address!!
                client.postalCode = clientDto.postalCode!!
                client.city = clientDto.city!!
                client.country = clientDto.country!!

                client
            }.onItem().invoke { _ -> logger.info("Client updated successfully") }.onFailure()
                .invoke { e -> logger.error("Couldn't update client: $e") }
        }
    }

    fun editNote(clientId: Long, note: String): Uni<Client> {
        if (clientId <= 0) {
            logger.error("Client ID is invalid")

            throw InvalidIdException("client")
        }

        return Panache.withTransaction {
            clientRepository.findById(clientId).onItem().transform { client ->
                if (client == null) {
                    logger.error("Client with ID $clientId is not registered")

                    throw InvalidIdException("client")
                }

                client.note = note

                client
            }.onItem().invoke { _ -> logger.info("Successfully edited note") }.onFailure()
                .invoke { e -> logger.error("Couldn't edit note: $e") }
        }
    }

    fun importClients(file: File): Multi<Uni<Client>> {
        val contents = CSVParser.parse(file, StandardCharsets.UTF_8, CSVFormat.EXCEL.withDelimiter(';'))
        val records = contents.records

        logger.info("Importing ${records.size - 1} clients")

        val multi: Multi<Uni<Client>> = Multi.createFrom().emitter { em ->
            // Remove column names
            for (record in records.drop(1)) {
                val id = record[0].toLong()
                val banner = record[6]
                val name = record[1]
                val fiscalNumber = record[5]
                val email = record[7]
                val phone = record[4]
                val address = record[2]
                val postalCode = record[3]
                val city = record[9]
                val country = if (record[8] == "PORTUGAL") {
                    "PT"
                } else if (record[8] == "ESPANHA") {
                    "ES"
                } else {
                    record[8]
                }

                val clientDto =
                    ClientDto(id, banner, name, fiscalNumber, email, phone, address, postalCode, city, country,"", mutableListOf())

                em.emit(clientRepository.findById(id).onItem().transformToUni { client ->
                    return@transformToUni if (client == null) {
                        add(clientDto)
                    } else {
                        update(clientDto)
                    }
                })
            }

            em.complete()
        }

        return multi.invoke { pipeline ->
            // Wait for all clients to finish
            pipeline.await().indefinitely()
        }
    }

    private fun createBannerIfNotExists(id: String): Uni<Banner> {
        return Panache.withTransaction {
            bannerRepository.findById(id).onItem().ifNull().switchTo {
                logger.info("Banner $id doesn't exist")

                val banner = Banner()
                banner.name = id
                banner.clients = mutableListOf()

                bannerRepository.persist(banner).invoke { _ -> logger.info("Successfully created banner") }.onFailure()
                    .invoke { e -> logger.error("Couldn't create banner: $e") }
            }
        }
    }
}
