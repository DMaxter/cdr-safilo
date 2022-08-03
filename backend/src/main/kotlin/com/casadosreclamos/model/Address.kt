package com.casadosreclamos.model;

import javax.persistence.*

@Entity
open class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long = 0

    @ManyToOne
    open lateinit var client: Client

    open lateinit var postalCode: String

    open lateinit var street: String
}
