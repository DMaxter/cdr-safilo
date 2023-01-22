package com.casadosreclamos.model.request

import javax.persistence.*

@Entity
open class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long = 0

    @Column(unique = true)
    open lateinit var name: String

    @ManyToMany
    open var additionalFinishings: MutableSet<Finishing> = mutableSetOf()

    @ManyToMany
    open var mandatoryFinishings: MutableSet<FinishingGroup> = mutableSetOf()
}