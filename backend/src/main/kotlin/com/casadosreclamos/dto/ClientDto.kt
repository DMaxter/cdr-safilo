package com.casadosreclamos.dto

import com.casadosreclamos.model.Client

data class ClientDto(
    var id: Long?,
    var name: String?,
    var addresses: List<AddressDto>?,
    var fiscalNumber: Long?,
    var email: String?,
    var phone: String?
) {
    constructor(client: Client) : this(
        client.id,
        client.name,
        client.addresses.stream().map { AddressDto(it) }.toList(),
        client.fiscalNumber,
        client.email,
        client.phone
    )
}
