package com.casadosreclamos.model

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
open class Banner {
    @Id
    open lateinit var name: String

    @OneToMany(mappedBy = "banner", orphanRemoval = true)
    open lateinit var clients: MutableList<Client>
}