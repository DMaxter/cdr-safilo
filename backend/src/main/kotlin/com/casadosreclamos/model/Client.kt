package com.casadosreclamos.model

import javax.persistence.Entity
import javax.persistence.Id

@Entity
class Client {
    @Id
    var id: Long = 0

    lateinit var name: String

    lateinit var address: String

    var fiscalNumber: Long = 0

    lateinit var postalCode: String

    lateinit var email: String

    lateinit var phone: String
}