package com.casadosreclamos.exception.fema

private const val MSG = "Couldn't open waybill"

class OpenWaybillException: FEMAException(MSG)