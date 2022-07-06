package com.casadosreclamos.model

import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
class PasswordToken {
    @EmbeddedId
    lateinit var id: PasswordTokenId

    lateinit var expiry: Date
}

@Embeddable
class PasswordTokenId: Serializable {
    lateinit var user: String

    lateinit var token: String
}