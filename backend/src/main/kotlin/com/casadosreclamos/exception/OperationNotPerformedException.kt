package com.casadosreclamos.exception

private const val MSG = "Operation could not be performed"

class OperationNotPerformedException : CDRException(MSG)