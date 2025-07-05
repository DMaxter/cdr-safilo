package com.casadosreclamos.exception

private const val MSG = " já existe"

class AlreadyExistsException(field: String) : CDRException(field + MSG)

