package com.casadosreclamos.dto

import com.casadosreclamos.model.request.Price
import java.util.stream.Collectors

data class PriceDto(
    var id: Long?,
    var material: Long?,
    var finishings: Set<FinishingDto>?,
    var costPerSquareMeter: Double?,
    var fixedCost: Double?
) {
    constructor(price: Price) : this(
        price.id,
        price.material.id,
        price.finishings.stream().map { FinishingDto(it) }.collect(Collectors.toSet()),
        price.costPerSquareMeter,
        price.fixedCost
    )
}