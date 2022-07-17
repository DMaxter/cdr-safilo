package com.casadosreclamos.exception

private const val MSG = "Invalid type of request: images need to be selected"

class InvalidRequestTypeException: CDRException(MSG)