package com.casadosreclamos.exception

private const val MSG = "ID inválido para "

class InvalidIdException(field: String) : CDRException(MSG + field)

