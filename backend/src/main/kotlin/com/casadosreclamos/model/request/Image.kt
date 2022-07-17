package com.casadosreclamos.model.request

import javax.persistence.*

@Entity
@Table(name = "Image")
open class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long = 0

    @ManyToOne
    open lateinit var brand: Brand

    open lateinit var link: String
}