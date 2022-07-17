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
    var created: Date?,
    var lastUpdate: Date?,
    var trackingCode: String?,
    var status: RequestStatus,
    var brand: String?,
    var material: String?,
    var measurements: Measurements?,
    var application: Boolean?,
    var images: RequestTypeDto?
) {
    constructor(request: Request) : this(
        request.id,
        request.requester.name,
        request.client.name,
        request.created,
        request.lastUpdate,
        request.trackingCode,
        request.status,
        request.info.brand.name,
        request.info.material.name,
        request.info.measurement,
        request.info.application,
        RequestTypeDto.from(request.info.type)
    )
}
