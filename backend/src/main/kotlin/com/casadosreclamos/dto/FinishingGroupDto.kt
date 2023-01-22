package com.casadosreclamos.dto

import com.casadosreclamos.model.request.FinishingGroup

data class FinishingGroupDto(var id: Long?, var name: String?, var finishings: List<FinishingDto>) {
    constructor(group: FinishingGroup) : this(group.id, group.name, group.finishings.stream().map { FinishingDto(it) }.toList())
}