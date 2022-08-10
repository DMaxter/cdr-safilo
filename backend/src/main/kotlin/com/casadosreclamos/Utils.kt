package com.casadosreclamos

val EMAIL_REGEX = Regex("^[\\w-.+]+@([\\w-]+\\.)+[\\w-]+$")
val PHONE_REGEX = Regex("(\\+|\\(\\+?\\d+\\))?[\\d\\s]{0,11}")
val POSTAL_REGEX = Regex("\\d{4}-\\d{3}")