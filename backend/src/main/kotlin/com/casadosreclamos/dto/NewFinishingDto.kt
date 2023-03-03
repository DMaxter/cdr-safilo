package com.casadosreclamos.dto

import com.casadosreclamos.model.request.Finishing

data class NewFinishingDto(var id: Long?, var name: String?, var cost: Double?, var materials: List<MaterialDto>)
