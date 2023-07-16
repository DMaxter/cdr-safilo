package com.casadosreclamos.exception.fema

private const val MSG = "Waybill is already opened for this request"

class WaybillAlreadyOpenedException: FEMAException(MSG)