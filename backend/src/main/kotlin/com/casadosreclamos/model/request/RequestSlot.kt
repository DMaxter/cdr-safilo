package com.casadosreclamos.model.request

import javax.persistence.*

@Entity
open class RequestSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long = 0

    @ManyToOne
    open lateinit var image: Image

    @ManyToOne
    open lateinit var material: Material

    @ManyToMany
    open var finishings: MutableSet<Finishing> = mutableSetOf()

    open lateinit var measurements: Measurements

    open var cost: Double = 0.0
}