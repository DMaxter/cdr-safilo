package com.casadosreclamos.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

class ResetPasswordDto {
    @JsonProperty("password")
    var password: String? = null

    constructor()

    @JsonCreator
    constructor(@JsonProperty("password") password: String?) {
        this.password = password
    }
}
