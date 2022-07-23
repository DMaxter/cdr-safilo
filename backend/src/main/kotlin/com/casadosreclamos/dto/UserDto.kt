package com.casadosreclamos.dto

import com.casadosreclamos.model.User

data class UserDto (val email: String?, val name: String?, val credits: Double?) {
    constructor(user: User): this(user.email, user.name, user.credits)
}