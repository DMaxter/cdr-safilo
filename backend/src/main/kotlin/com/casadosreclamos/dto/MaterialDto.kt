package com.casadosreclamos.dto

import com.casadosreclamos.model.request.Material

data class MaterialDto(var id: Long?, var name: String?, var cost: Double?) {
    constructor(material: Material): this(material.id, material.name, material.cost)
}
