package com.casadosreclamos.utils

import com.casadosreclamos.dto.CountryDto

val COUNTRIES =
        listOf(
                CountryDto("PT", "Portugal"),
                CountryDto("ES", "Espanha"),
        )

private val COUNTRY_ALIASES =
        mapOf(
                "PORTUGAL" to "PT",
                "ESPANHA" to "ES",
                "ESPAÑA" to "ES",
                "SPAIN" to "ES",
        )

fun normalizeCountry(value: String?): String? {
    val trimmed = value?.trim()

    if (trimmed.isNullOrEmpty()) {
        return null
    }

    val country =
            COUNTRIES.firstOrNull {
                it.code.equals(trimmed, ignoreCase = true) || it.name.equals(trimmed, ignoreCase = true)
            }

    if (country != null) {
        return country.code
    }

    return COUNTRY_ALIASES[trimmed.uppercase()]
}

fun isSupportedCountry(value: String?): Boolean {
    return normalizeCountry(value) != null
}
