package com.casadosreclamos.model.request

import org.hibernate.Hibernate
import javax.persistence.*

@Entity
open class Finishing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long = 0

    @Column(unique = true)
    open lateinit var name: String

    open var cost: Double = 0.0

    @ManyToMany
    @JoinTable(
        name = "Finishing_exclusiveMaterials",
        joinColumns = [JoinColumn(name = "finishing_id")],
        inverseJoinColumns = [JoinColumn(name = "exclusiveMaterials_id")]
    )
    open lateinit var exclusiveMaterials: MutableSet<Material>

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Finishing

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}