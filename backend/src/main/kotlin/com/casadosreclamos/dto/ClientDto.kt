package com.casadosreclamos.dto

data class ClientDto(
    var id: Long?,
    var name: String?,
    var address: String?,
    var fiscalNumber: Long?,
    var postalCode: String?,
    var email: String?,
    var phone: String?
)
