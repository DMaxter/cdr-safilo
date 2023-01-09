package com.casadosreclamos.dto

import com.casadosreclamos.model.request.Finishing

data class FinishingDto(var id: Long?, var name: String?, var cost: Double?) {
    constructor(finishing: Finishing) : this(finishing.id, finishing.name, finishing.cost)
}
