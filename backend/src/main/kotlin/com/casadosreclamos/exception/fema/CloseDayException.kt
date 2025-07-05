package com.casadosreclamos.exception.fema

private const val MSG = "Não foi possível efetuar o fecho de dia"

class CloseDayException : FEMAException(MSG)

