package com.casadosreclamos.exception

private const val MSG = "Not enought credits to make a request"

class NotEnoughCreditsException: CDRException(MSG)