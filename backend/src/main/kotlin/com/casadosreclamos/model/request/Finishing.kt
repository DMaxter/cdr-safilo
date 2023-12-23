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

    open var obsolete: Boolean = false

    override fun toString(): String {
       return "ID: $id, NAME: $name"
    }
}
