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

    fun getAll(): Multi<ClientDto> {
        return clientRepository.streamAll().map { ClientDto(it) }
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
    fun register(clientDto: ClientDto): Uni<Client> {
        val client = Client()

        if (clientDto.id == null || clientDto.id!! <= 0) {
            throw InvalidIdException("client")
        } else if (clientDto.banner == null || clientDto.banner!!.isEmpty()) {
            throw InvalidBannerException()
        } else if (clientDto.email == null || clientDto.email!!.isEmpty() || !EMAIL_REGEX.matches(clientDto.email!!)) {
            throw InvalidEmailException()
        } else if (clientDto.name == null || clientDto.name!!.isEmpty()) {
            throw InvalidNameException()
        } else if (clientDto.fiscalNumber == null) {
            throw InvalidFiscalNumberException()
        } else if (clientDto.phone == null || clientDto.phone!!.isEmpty() || !PHONE_REGEX.matches(clientDto.phone!!)) {
            throw InvalidPhoneException()
        } else if (clientDto.address == null || clientDto.address!!.isEmpty()) {
            throw InvalidAddressException()
        } else if (clientDto.postalCode == null || clientDto.postalCode!!.isEmpty()) {
            throw InvalidPostalCodeException()
        }


        client.id = clientDto.id!!
        client.email = clientDto.email!!
        client.name = clientDto.name!!
        client.fiscalNumber = clientDto.fiscalNumber!!
        client.phone = clientDto.phone!!
        client.address = clientDto.address!!
        client.postalCode = clientDto.postalCode!!

        return createBannerIfNotExists(clientDto.banner!!).onItem().transformToUni { banner ->
            client.banner = banner

            Panache.withTransaction {
                clientRepository.persist(client)
            }
        }
    }

    fun importClients(file: File): Multi<Uni<Client>> {
        val contents = CSVParser.parse(file, Charset.defaultCharset(), CSVFormat.EXCEL)

        val multi: Multi<Uni<Client>> = Multi.createFrom().emitter { em ->
            // Remove column names
            for (record in contents.records.drop(1)) {
                val id = record[0].toLong()
                val banner = record[7]
                val name = record[1]
                val fiscalNumber = record[6].toLong()
                val email = record[8]
                val phone = record[5]
                val address = record[3]
                val postalCode = record[4]

                val clientDto = ClientDto(id, banner, name, fiscalNumber, email, phone, address, postalCode)


                em.emit(clientRepository.findById(id).onItem().ifNull().switchTo {
                    return@switchTo register(clientDto)
                })
            }

            em.complete()
        }

        return multi.invoke { pipeline ->
            pipeline.await().indefinitely()
        }
    }

    private fun createBannerIfNotExists(id: String): Uni<Banner> {
        return Panache.withTransaction {
            bannerRepository.findById(id).onItem().ifNull().switchTo {
                val banner = Banner()
                banner.name = id
                banner.clients = mutableListOf()

                bannerRepository.persist(banner)
            }
        }
    }
}
