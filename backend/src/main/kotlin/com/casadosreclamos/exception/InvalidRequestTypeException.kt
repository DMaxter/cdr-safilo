package com.casadosreclamos.exception

private const val MSG = "Tipo de pedido inválido"

class InvalidRequestTypeException : CDRException(MSG)

