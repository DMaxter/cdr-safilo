package com.casadosreclamos.model.request

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
open class RequestSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long = 0

    @ManyToOne
    open lateinit var image: Image

    @ManyToOne
    open lateinit var material: Material

    @ManyToOne
    open var finishing: Finishing? = null

    open lateinit var measurements: Measurements

    open var cost: Double? = null
}