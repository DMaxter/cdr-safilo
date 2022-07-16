package com.casadosreclamos.model

import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
open class PasswordToken {
    @EmbeddedId
    open lateinit var id: PasswordTokenId

    open lateinit var expiry: Date
}

@Embeddable
class PasswordTokenId: Serializable {
    lateinit var user: String

    lateinit var token: String

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PasswordTokenId

        if (user != other.user) return false
        if (token != other.token) return false

        return true
    }

    override fun hashCode(): Int {
        var result = user.hashCode()
        result = 31 * result + token.hashCode()
        return result
    }
}