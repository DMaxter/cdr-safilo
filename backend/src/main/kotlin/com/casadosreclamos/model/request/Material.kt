package com.casadosreclamos.model.request

import javax.persistence.*

@Entity
open class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long = 0

    @Column(unique = true)
    open lateinit var name: String

    @ManyToMany(fetch = FetchType.EAGER)
    open var additionalFinishings: MutableSet<Finishing> = mutableSetOf()

    @ManyToMany(fetch = FetchType.EAGER)
    open var mandatoryFinishings: MutableSet<FinishingGroup> = mutableSetOf()
}