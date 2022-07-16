package com.casadosreclamos.exception

private const val MSG = "Invalid id for "

class InvalidIdException(field: String): CDRException(MSG + field)