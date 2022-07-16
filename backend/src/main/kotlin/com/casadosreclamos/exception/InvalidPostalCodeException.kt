package com.casadosreclamos.exception

private const val MSG = "Invalid postal code"

class InvalidPostalCodeException: CDRException(MSG)