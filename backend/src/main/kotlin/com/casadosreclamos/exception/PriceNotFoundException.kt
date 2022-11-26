package com.casadosreclamos.exception

private const val MSG = "Could not find price for given measurements"

class PriceNotFoundException : CDRException(MSG)