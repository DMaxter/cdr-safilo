package com.casadosreclamos.model.request

import javax.persistence.*

@Entity
open class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long = 0

    @Column(unique = true)
    open lateinit var name: String

    open var cost: Double = 0.0
}