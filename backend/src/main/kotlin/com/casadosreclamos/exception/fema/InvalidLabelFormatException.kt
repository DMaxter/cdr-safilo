package com.casadosreclamos.exception.fema

private const val MSG = "O formato da etiqueta é inválido"

class InvalidLabelFormatException : FEMAException(MSG)

