package com.casadosreclamos.dto

import com.casadosreclamos.model.request.FinishingGroup
import java.util.stream.Collectors

data class FinishingGroupDto(var id: Long?, var name: String?, var finishings: Set<FinishingDto>?) {
    constructor(group: FinishingGroup) : this(group.id, group.name, group.finishings.stream().map { FinishingDto(it) }.collect(
        Collectors.toSet()))
}