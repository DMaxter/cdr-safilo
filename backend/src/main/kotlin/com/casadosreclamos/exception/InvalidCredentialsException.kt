package com.casadosreclamos.exception

private const val MSG = "Invalid credentials"

class InvalidCredentialsException: CDRException(MSG)