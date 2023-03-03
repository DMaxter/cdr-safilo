package com.casadosreclamos.exception

private const val MSG = "Missing mandatory finishing"

class MandatoryFinishingMissingException : CDRException(MSG)
