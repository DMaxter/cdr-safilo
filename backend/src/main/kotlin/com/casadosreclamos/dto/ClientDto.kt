package com.casadosreclamos.dto

import com.casadosreclamos.model.Client

data class ClientDto(
    var id: Long?,
    var banner: String?,
    var name: String?,
    var fiscalNumber: String?,
    var email: String?,
    var phone: String?,
    var address: String?,
    var postalCode: String?
) {
    constructor(client: Client) : this(
        client.id,
        client.banner.name,
        client.name,
        client.fiscalNumber,
        client.email,
        client.phone,
        client.address,
        client.postalCode
    )
}
