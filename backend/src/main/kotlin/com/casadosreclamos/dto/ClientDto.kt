package com.casadosreclamos.dto

import com.casadosreclamos.model.Client
import java.util.stream.Collectors

data class ClientDto(
    var id: Long?,
    var banner: String?,
    var name: String?,
    var fiscalNumber: String?,
    var email: String?,
    var phone: String?,
    var address: String?,
    var postalCode: String?,
    var city: String?,
    var country: String?,
    var note: String?,
    var images: List<ImageDto>?
) {
    constructor(client: Client) : this(
        client.id,
        client.banner.name,
        client.name,
        client.fiscalNumber,
        client.email,
        client.phone,
        client.address,
        client.postalCode,
        client.city,
        client.country,
        client.note,
        client.images.stream().map { ImageDto(it) }.collect(Collectors.toList())
    )
}
