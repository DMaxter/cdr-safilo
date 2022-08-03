package com.casadosreclamos.dto

import com.casadosreclamos.model.Address

data class AddressDto(var id: Long?, var address: String?, var postalCode: String?) {
    constructor(address: Address): this(address.id, address.street, address.postalCode)
}
