package com.casadosreclamos.dto

import com.casadosreclamos.model.request.Material

data class MaterialDto(
    var id: Long?,
    var name: String?,
    var mandatoryFinishings: List<FinishingGroupDto>?,
    var additionalFinishings: List<FinishingDto>?
) {
    constructor(material: Material) : this(
        material.id,
        material.name,
        material.mandatoryFinishings.stream().map { FinishingGroupDto(it) }.toList(),
        material.additionalFinishings.stream().map { FinishingDto(it) }.toList()
    )
}
