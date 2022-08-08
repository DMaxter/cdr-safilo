package com.casadosreclamos.dto

import com.casadosreclamos.model.Plafond

data class PlafondDto(val brand: String?, val amount: Double?) {
    constructor(plafond: Plafond): this(plafond.brand.name, plafond.amount)
}
