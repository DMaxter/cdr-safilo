package com.casadosreclamos.exception

private const val MSG = "Acabamento obrigatório em falta"

class MandatoryFinishingMissingException : CDRException(MSG)
