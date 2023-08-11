package com.casadosreclamos.exception.fema

class GetWaybillException(val response: String): FEMAException(response)