package com.casadosreclamos.service

import com.casadosreclamos.EMAIL_REGEX
import com.casadosreclamos.PHONE_REGEX
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
import java.nio.charset.Charset
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
        } else if (clientDto.banner == null || clientDto.banner!!.isEmpty()) {
            logger.error("Client Banner is null or empty")

            throw InvalidBannerException()
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
        }


        client.id = clientDto.id!!
        client.email = clientDto.email!!
        client.name = clientDto.name!!
        client.fiscalNumber = clientDto.fiscalNumber!!
        client.phone = clientDto.phone!!
        client.address = clientDto.address!!
        client.postalCode = clientDto.postalCode!!
        client.images = mutableSetOf()

        return createBannerIfNotExists(clientDto.banner!!).onItem().transformToUni { banner ->
            client.banner = banner

            Panache.withTransaction {
                clientRepository.persist(client).onItem().invoke { _ -> logger.info("Successfully registered client") }
                    .onFailure().invoke { e -> logger.error("Couldn't create client: $e") }
            }
        }
    }

    fun importClients(file: File): Multi<Uni<Client>> {
        val contents = CSVParser.parse(file, Charset.forName("UTF-8"), CSVFormat.EXCEL.withDelimiter(';'))

        logger.info("Importing ${contents.records.size - 1} clients")

        val multi: Multi<Uni<Client>> = Multi.createFrom().emitter { em ->
            // Remove column names
            for (record in contents.records.drop(1)) {
                val id = record[0].toLong()
                val banner = record[7]
                val name = record[1]
                val fiscalNumber = record[6]
                val email = record[8]
                val phone = record[5]
                val address = record[3]
                val postalCode = record[4]

                val clientDto = ClientDto(id, banner, name, fiscalNumber, email, phone, address, postalCode, mutableListOf())


                em.emit(clientRepository.findById(id).onItem().ifNull().switchTo {
                    return@switchTo add(clientDto)
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
