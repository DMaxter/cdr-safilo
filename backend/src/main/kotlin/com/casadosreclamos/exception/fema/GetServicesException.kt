package com.casadosreclamos.exception.fema

class GetServicesException(val response: String): FEMAException(response)