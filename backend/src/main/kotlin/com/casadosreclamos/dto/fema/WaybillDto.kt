package com.casadosreclamos.dto.fema

data class WaybillDto(
    var reference: String?,
    val service: Service?,
    var source: Contact?,
    var destination: Contact?,
    val items: Long?,
    val packaging: PackageType?,
    val totalWeight: Double?,
    var description: String?,
    val labelFormat: LabelFormat?,
    val dimensions: Dimensions?
)