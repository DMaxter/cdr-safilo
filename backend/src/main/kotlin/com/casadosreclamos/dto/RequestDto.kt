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
    var brand: String?,
    var material: MaterialDto?,
    var measurements: Measurements?,
    var images: RequestTypeDto?,
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
        request.info.brand.name,
        MaterialDto(request.info.material),
        request.info.measurement,
        RequestTypeDto.from(request.info.type),
        request.cost,
        request.observations
    )
}
