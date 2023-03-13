package com.casadosreclamos.model.request

import com.casadosreclamos.model.Client
import javax.persistence.*

@Entity
@Table(name = "Image")
open class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long = 0

    @ManyToOne
    open var brand: Brand? = null

    @ManyToOne
    open var client: Client? = null

    open var obsolete: Boolean = false

    open lateinit var link: String
}