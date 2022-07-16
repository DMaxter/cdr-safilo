package com.casadosreclamos.exception

private const val MSG = "Invalid email"

class InvalidEmailException: CDRException(MSG)