package com.casadosreclamos.dto

import com.casadosreclamos.model.request.Material

data class MaterialDto(var id: Long?, var name: String?, var finishings: List<FinishingDto>?) {
    constructor(material: Material) : this(
        material.id,
        material.name,
        material.finishings.stream().map { FinishingDto(it) }.toList()
    )
}
