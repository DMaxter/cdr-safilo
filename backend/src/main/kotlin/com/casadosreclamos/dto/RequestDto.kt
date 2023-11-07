package com.casadosreclamos.dto

import com.casadosreclamos.model.Request
import com.casadosreclamos.model.request.Measurements
import com.casadosreclamos.model.request.RequestStatus
import com.casadosreclamos.model.request.RequestType
import java.util.*

data class RequestDto(
    var id: Long?,
    var user: String?,
    var client: ClientDto?,
    var created: Date?,
    var lastUpdate: Date?,
    var trackingCode: Long?,
    var status: RequestStatus,
    var type: RequestTypeDto?,
    var cost: Double?,
    var observations: String?,
    var application: Boolean?,
    var brand: BrandDto?
) {
    constructor(request: Request) : this(
        request.id,
        request.requester.name,
        ClientDto(request.client),
        request.created,
        request.lastUpdate,
        request.trackingCode,
        request.status,
        RequestTypeDto.from(request.type!!),
        request.cost,
        request.observations,
        request.application,
        BrandDto(request.brand.id, request.brand.name, null)
    )
}
