package com.casadosreclamos.exception.fema

private const val MSG = "Label format is invalid"

class InvalidLabelFormatException: FEMAException(MSG)