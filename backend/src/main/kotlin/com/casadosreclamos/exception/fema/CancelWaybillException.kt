package com.casadosreclamos.exception.fema

class CancelWaybillException(val response: String) : FEMAException(response) {
}