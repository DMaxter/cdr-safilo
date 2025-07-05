package com.casadosreclamos.exception

private const val MSG = "Sem plafond suficiente para realizar o pedido"

class NotEnoughCreditsException : CDRException(MSG)

