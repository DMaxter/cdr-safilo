package com.casadosreclamos.exception.fema

private const val MSG = "Couldn't close day"

class CloseDayException: FEMAException(MSG)