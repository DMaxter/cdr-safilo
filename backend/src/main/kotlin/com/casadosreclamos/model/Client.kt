package com.casadosreclamos.model

import com.casadosreclamos.model.request.Image
import javax.persistence.*

@Entity
open class Client {
    @Id
    open var id: Long = 0

    @ManyToOne
    open var banner: Banner? = null

    open lateinit var name: String

    open lateinit var fiscalNumber: String

    open lateinit var email: String

    open lateinit var phone: String

    open lateinit var address: String

    open lateinit var postalCode: String

    open lateinit var city: String

    open lateinit var country: String

    open var note: String = ""

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "client", orphanRemoval = true)
    open lateinit var images: MutableSet<Image>
}