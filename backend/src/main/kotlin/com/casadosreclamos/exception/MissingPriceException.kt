package com.casadosreclamos.exception

private const val MSG = "Missing price for finishings/material"

class MissingPriceException : CDRException(MSG)