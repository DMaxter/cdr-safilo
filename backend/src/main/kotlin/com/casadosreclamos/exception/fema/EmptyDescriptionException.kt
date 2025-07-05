package com.casadosreclamos.exception.fema

private const val MSG = "A descrição é inexistente ou está vazia"

class EmptyDescriptionException : FEMAException(MSG)

