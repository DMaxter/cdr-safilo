package com.casadosreclamos.dto

import com.casadosreclamos.model.request.Price

data class PriceDto(
    var id: Long?,
    var material: Long?,
    var finishings: List<FinishingDto>?,
    var costPerSquareMeter: Double?,
    var fixedCost: Double?
) {
    constructor(price: Price) : this(
        price.id,
        price.material.id,
        price.finishings.stream().map { FinishingDto(it) }.toList(),
        price.costPerSquareMeter,
        price.fixedCost
    )
}