package com.casadosreclamos.exception.fema

private const val MSG = "Description is empty or non existent"

class EmptyDescriptionException: FEMAException(MSG)