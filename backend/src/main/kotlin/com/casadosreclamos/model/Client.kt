package com.casadosreclamos.model

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
open class Client {
    @Id
    open var id: Long = 0

    open lateinit var name: String

    @OneToMany(mappedBy = "client", orphanRemoval = true)
    open lateinit var addresses: MutableList<Address>

    open var fiscalNumber: Long = 0

    open lateinit var email: String

    open lateinit var phone: String
}