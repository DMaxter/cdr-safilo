package com.casadosreclamos.dto

import com.casadosreclamos.model.request.Material
import java.util.stream.Collectors

data class MaterialDto(
    var id: Long?,
    var name: String?,
    var mandatoryFinishings: Set<FinishingGroupDto>?,
    var additionalFinishings: Set<FinishingDto>?
) {
    constructor(material: Material) : this(
        material.id,
        material.name,
        material.mandatoryFinishings.stream().map { FinishingGroupDto(it) }.collect(Collectors.toSet()),
        material.additionalFinishings.stream().map { FinishingDto(it) }.collect(Collectors.toSet())
    )
}
