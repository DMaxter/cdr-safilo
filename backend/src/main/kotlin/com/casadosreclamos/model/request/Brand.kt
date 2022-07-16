package com.casadosreclamos.model.request

import javax.persistence.*

@Entity
open class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long = 0

    @Column(unique = true)
    open lateinit var name: String

    @OneToMany(mappedBy = "brand", orphanRemoval = true)
    open lateinit var images: MutableList<Image>
}