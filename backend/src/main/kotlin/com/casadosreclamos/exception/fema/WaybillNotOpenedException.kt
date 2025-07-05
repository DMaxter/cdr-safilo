package com.casadosreclamos.exception.fema

private const val MSG = "Não existe uma carta de porte para este pedido"

class WaybillNotOpenedException : FEMAException(MSG)

