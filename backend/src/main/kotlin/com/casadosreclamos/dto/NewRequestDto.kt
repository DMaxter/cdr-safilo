package com.casadosreclamos.dto

import com.casadosreclamos.model.request.Measurements
import org.eclipse.microprofile.openapi.annotations.media.Schema

data class NewRequestDto(
    var clientId: Long?,
    var brandId: Long?,
    var materialId: Long?,
    var amount: Int?,
    var measurements: Measurements?,
    var application: Boolean,

    @Schema(oneOf = [OneFace::class, TwoFaces::class, LeftShowcase::class, RightShowcase::class])
    var images: RequestTypeDto?
)
