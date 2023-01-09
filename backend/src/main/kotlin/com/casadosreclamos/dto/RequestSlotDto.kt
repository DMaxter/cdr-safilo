package com.casadosreclamos.dto

import com.casadosreclamos.model.request.Measurements
import com.casadosreclamos.model.request.RequestSlot

data class RequestSlotDto(
    var image: ImageDto?, var measurements: Measurements?, var material: MaterialDto?, var finishing: FinishingDto?
) {
    constructor(slot: RequestSlot) : this(
        ImageDto(slot.image), slot.measurements, MaterialDto(slot.material), slot.finishing?.let { FinishingDto(it) }
    )
}