package com.casadosreclamos.dto

import org.eclipse.microprofile.openapi.annotations.media.Schema

data class NewRequestDto(
    var clientId: Long?,
    var amount: Int?,
    var addressId: Long?,
    var observations: String?,

    @Schema(oneOf = [OneFace::class, TwoFaces::class, LeftShowcase::class, RightShowcase::class])
    var type: RequestTypeDto?
)