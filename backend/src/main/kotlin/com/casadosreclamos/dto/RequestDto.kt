package com.casadosreclamos.dto

import com.casadosreclamos.model.Request
import com.casadosreclamos.model.request.Measurements
import com.casadosreclamos.model.request.RequestStatus
import com.casadosreclamos.model.request.RequestType
import java.util.*

data class RequestDto(
    var id: Long?,
    var user: String?,
    var client: String?,
    var address: AddressDto?,
    var created: Date?,
    var lastUpdate: Date?,
    var trackingCode: String?,
    var status: RequestStatus,
    var type: RequestTypeDto?,
    var cost: Double?,
    var observations: String?
) {
    constructor(request: Request) : this(
        request.id,
        request.requester.name,
        request.client.name,
        AddressDto(request.address),
        request.created,
        request.lastUpdate,
        request.trackingCode,
        request.status,
        RequestTypeDto.from(request.type),
        request.cost,
        request.observations
    )
}
