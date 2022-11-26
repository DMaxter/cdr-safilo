package com.casadosreclamos.model.request

import javax.persistence.EmbeddedId
import javax.persistence.Entity

@Entity
open class Price {
    @EmbeddedId
    open lateinit var measurements: Measurements

    open var cost: Double = 0.0
}