package com.casadosreclamos.dto

import com.casadosreclamos.model.request.Price

data class PriceDto(var width: Double?, var height: Double?, var material: Long?, var cost: Double?) {
    constructor(price: Price) : this(
        price.id.measurements.width, price.id.measurements.height, price.id.materialId, price.cost
    )
}