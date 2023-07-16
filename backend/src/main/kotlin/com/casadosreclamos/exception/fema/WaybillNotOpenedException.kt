package com.casadosreclamos.exception.fema

private const val MSG = "A waybill is not open for this request"

class WaybillNotOpenedException: FEMAException(MSG)