package com.casadosreclamos.exception.fema

class OpenWaybillException(val response: String): FEMAException(response)