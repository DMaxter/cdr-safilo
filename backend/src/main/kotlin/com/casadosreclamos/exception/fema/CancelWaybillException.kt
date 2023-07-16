package com.casadosreclamos.exception.fema

private const val MSG = "Couldn't cancel waybill: "

class CancelWaybillException(val response: String) : FEMAException(MSG + response) {
}