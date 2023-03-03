package com.casadosreclamos.exception

private const val MSG = " already exists"

class AlreadyExistsException(field: String): CDRException(field + MSG)