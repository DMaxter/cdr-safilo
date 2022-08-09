package com.casadosreclamos.dto

import com.casadosreclamos.model.User

data class UserDto(val email: String?, val name: String?, val credits: MutableSet<PlafondDto>?) {
    constructor(user: User) : this(
        user.email,
        user.name,
        user.credits.stream().map { PlafondDto(it) }.toList().toMutableSet()
    )
}