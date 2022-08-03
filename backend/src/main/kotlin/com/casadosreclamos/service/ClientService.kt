package com.casadosreclamos.service

import com.casadosreclamos.dto.AddressDto
import com.casadosreclamos.dto.ClientDto
import com.casadosreclamos.exception.*
import com.casadosreclamos.model.Address
import com.casadosreclamos.model.Client
import com.casadosreclamos.repo.AddressRepository
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

    @Inject
    lateinit var addressRepository: AddressRepository

    fun getAll(): Multi<ClientDto> {
        return clientRepository.streamAllWithAddresses().map { ClientDto(it) }
    }

    @Throws(
        InvalidIdException::class,
        InvalidEmailException::class,
        InvalidNameException::class,
    )
    fun register(clientDto: ClientDto): Uni<Response> {
        val client = Client()

        // TODO: verify valid email
        // TODO: verify valid phone
        // TODO: verify valid postal code
        if (clientDto.id == null || clientDto.id!! <= 0) {
            throw InvalidIdException("client")
        } else if (clientDto.email == null || clientDto.email!!.isEmpty()) {
            throw InvalidEmailException()
        } else if (clientDto.name == null || clientDto.name!!.isEmpty()) {
            throw InvalidNameException()
        } else if (clientDto.fiscalNumber == null) {
            throw InvalidFiscalNumberException()
        } else if (clientDto.phone == null || clientDto.phone!!.isEmpty()) {
            throw InvalidPhoneException()
        }

        client.id = clientDto.id!!
        client.email = clientDto.email!!
        client.name = clientDto.name!!
        client.fiscalNumber = clientDto.fiscalNumber!!
        client.phone = clientDto.phone!!
        client.addresses = mutableListOf()

        return Panache.withTransaction { clientRepository.persist(client) }.onItem().transform { Response.ok().build() }
            .onFailure().recoverWithItem { e ->
                logger.error(e)
                Response.serverError().build()
            }
    }

    @Throws(InvalidAddressException::class, InvalidPostalCodeException::class)
    fun addAddress(clientId: Long, addressDto: AddressDto): Uni<Response> {
        val address = Address()

        if (clientId <= 0) {
            throw InvalidIdException("client")
        } else if (addressDto.address == null || addressDto.address!!.isEmpty()) {
            throw InvalidAddressException()
        } else if (addressDto.postalCode == null || addressDto.postalCode!!.isEmpty()) {
            throw InvalidPostalCodeException()
        }

        address.street = addressDto.address!!
        address.postalCode = addressDto.postalCode!!

        return Panache.withTransaction {
            clientRepository.findByIdWithAddresses(clientId).onItem().transformToUni { client ->
                address.client = client
                client.addresses.add(address)
                return@transformToUni addressRepository.persist(address)
            }
        }.onItem().transform { Response.ok().build() }.onFailure().recoverWithItem { e ->
            logger.error(e)
            Response.serverError().build()
        }
    }
}
