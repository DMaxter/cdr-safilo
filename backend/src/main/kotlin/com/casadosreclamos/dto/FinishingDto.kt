package com.casadosreclamos.dto

import com.casadosreclamos.model.request.Finishing

data class FinishingDto(var id: Long?, var name: String?) {
    constructor(finishing: Finishing) : this(finishing.id, finishing.name)
}
