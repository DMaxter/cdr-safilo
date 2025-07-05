package com.casadosreclamos.exception

private const val MSG = "Impossível executar a operação"

class OperationNotPerformedException : CDRException(MSG)

