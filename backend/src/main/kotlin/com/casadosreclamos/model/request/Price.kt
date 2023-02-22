package com.casadosreclamos.model.request

import java.io.Serializable
import javax.persistence.*

@Entity
open class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long = 0

    @ManyToOne
    open lateinit var material: Material

    @ManyToMany
    open var finishings: MutableSet<Finishing> = mutableSetOf()

    open var costPerSquareMeter: Double = 0.0

    open var fixedCost: Double = 0.0

    override fun toString(): String {
        return "ID: $id, MATERIAL: $material, COSTM2: $costPerSquareMeter, COSTFIX: $fixedCost, finishings: ($finishings)"
    }
}