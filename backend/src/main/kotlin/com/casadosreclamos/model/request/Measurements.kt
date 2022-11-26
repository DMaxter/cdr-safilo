package com.casadosreclamos.model.request

import java.io.Serializable
import javax.persistence.Embeddable

@Embeddable
class Measurements: Serializable {
    var width: Double = 0.0
    var height: Double = 0.0
}
