package com.casadosreclamos.exception.fema

private const val MSG = "Couldn't get waybill"

class GetWaybillException: FEMAException(MSG)