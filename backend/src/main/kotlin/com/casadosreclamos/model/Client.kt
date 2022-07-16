package com.casadosreclamos.model

import javax.persistence.Entity
import javax.persistence.Id

@Entity
open class Client {
    @Id
    open var id: Long = 0

    open lateinit var name: String

    open lateinit var address: String

    open var fiscalNumber: Long = 0

    open lateinit var postalCode: String

    open lateinit var email: String

    open lateinit var phone: String
}