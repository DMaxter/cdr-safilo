package com.casadosreclamos.exception

private const val MSG = "Preço não existente para acabamentos ou material"

class MissingPriceException : CDRException(MSG)

