package com.casadosreclamos.service

import com.casadosreclamos.dto.ClientDto
import com.casadosreclamos.exception.*
import com.casadosreclamos.model.Client
import com.casadosreclamos.repo.ClientRepository
import io.quarkus.hibernate.reactive.panache.Panache
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import org.jboss.logging.Logger

import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.core.Response

@ApplicationScoped
class ClientService {
    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var clientRepository: ClientRepository

    fun getAll(): Multi<Client> {
        return clientRepository.streamAll()
    }

    @Throws(
        InvalidIdException::class,
        InvalidEmailException::class,
        InvalidNameException::class,
        InvalidAddressException::class
    )
    fun register(clientDto: ClientDto): Uni<Response> {
        logger.info("Registering client ${clientDto.name}")

        val client = Client()

        // TODO: verify valid email
        // TODO: verify valid phone
        // TODO: verify valid postal code
        if (clientDto.id == null || clientDto.id!! <= 0) {
            throw InvalidIdException()
        } else if (clientDto.email == null || clientDto.email!!.isEmpty()) {
            throw InvalidEmailException()
        } else if (clientDto.name == null || clientDto.name!!.isEmpty()) {
            throw InvalidNameException()
        } else if (clientDto.address == null || clientDto.address!!.isEmpty()) {
            throw InvalidAddressException()
        } else if (clientDto.fiscalNumber == null) {
            throw InvalidFiscalNumberException()
        } else if (clientDto.phone == null || clientDto.phone!!.isEmpty()) {
            throw InvalidPhoneException()
        } else if (clientDto.postalCode == null || clientDto.postalCode!!.isEmpty()) {
            throw InvalidPostalCodeException()
        }

        client.id = clientDto.id!!
        client.email = clientDto.email!!
        client.name = clientDto.name!!
        client.address = clientDto.address!!
        client.fiscalNumber = clientDto.fiscalNumber!!
        client.phone = clientDto.phone!!
        client.postalCode = clientDto.postalCode!!

        return Panache.withTransaction { clientRepository.persist(client) }.onItem().transform { Response.ok().build() }
            .onFailure().recoverWithItem { _ -> Response.serverError().build() }
    }
}
