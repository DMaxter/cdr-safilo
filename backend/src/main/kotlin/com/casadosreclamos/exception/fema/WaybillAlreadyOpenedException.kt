package com.casadosreclamos.exception.fema

private const val MSG = "Uma carta de porte já foi aberta para este pedido"

class WaybillAlreadyOpenedException : FEMAException(MSG)

