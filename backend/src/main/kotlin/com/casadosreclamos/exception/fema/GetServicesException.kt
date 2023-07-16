package com.casadosreclamos.exception.fema

private const val MSG = "Couldn't get available services for request"

class GetServicesException: FEMAException(MSG)