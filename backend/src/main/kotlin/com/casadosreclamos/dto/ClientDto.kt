package com.casadosreclamos.dto

import com.casadosreclamos.model.Client

data class ClientDto(
    var id: Long?,
    var name: String?,
    var address: String?,
    var fiscalNumber: Long?,
    var postalCode: String?,
    var email: String?,
    var phone: String?
) {
    constructor(client: Client) : this(
        client.id,
        client.name,
        client.address,
        client.fiscalNumber,
        client.postalCode,
        client.email,
        client.phone
    )
}
