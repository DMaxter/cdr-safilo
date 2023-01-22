package com.casadosreclamos.dto

import com.casadosreclamos.model.request.Measurements
import com.casadosreclamos.model.request.RequestSlot
import java.util.stream.Collectors

data class RequestSlotDto(
    var image: ImageDto?,
    var measurements: Measurements?,
    var material: MaterialDto?,
    var finishings: MutableSet<FinishingDto>?
) {
    constructor(slot: RequestSlot) : this(
        ImageDto(slot.image),
        slot.measurements,
        MaterialDto(slot.material),
        slot.finishings.stream().map { FinishingDto(it) }.collect(
            Collectors.toSet()
        )
    )
}