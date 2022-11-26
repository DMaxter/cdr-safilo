package com.casadosreclamos.dto

import com.casadosreclamos.model.request.Price

data class PriceDto (var width: Double?, var height: Double?, var cost: Double?) {
    constructor(price: Price) : this(price.measurements.width, price.measurements.height, price.cost)
}