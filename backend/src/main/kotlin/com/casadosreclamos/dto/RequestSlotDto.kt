package com.casadosreclamos.dto

import com.casadosreclamos.model.request.Measurements
import com.casadosreclamos.model.request.RequestSlot

data class RequestSlotDto(
    var brand: BrandDto?,

    var image: ImageDto?,

    var measurements: Measurements?,

    var material: MaterialDto?
) {
    constructor(slot: RequestSlot) : this(
        BrandDto(slot.brand.id, slot.brand.name, null),
        ImageDto(slot.image),
        slot.measurements,
        MaterialDto(slot.material)
    )
}