package com.casadosreclamos.dto

import com.casadosreclamos.model.request.Finishing

data class FinishingDto(var id: Long?, var name: String?, var obsolete: Boolean?) {
    constructor(finishing: Finishing) : this(finishing.id, finishing.name, finishing.obsolete)
}
