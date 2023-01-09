package com.casadosreclamos.model.request

import org.hibernate.Hibernate
import javax.persistence.*

@Entity
open class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long = 0

    @Column(unique = true)
    open lateinit var name: String


    @ManyToMany
    @JoinTable(
        name = "Material_finishings",
        joinColumns = [JoinColumn(name = "material_id")],
        inverseJoinColumns = [JoinColumn(name = "finishings_id")]
    )
    open var finishings: MutableSet<Finishing> = mutableSetOf()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Material

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}