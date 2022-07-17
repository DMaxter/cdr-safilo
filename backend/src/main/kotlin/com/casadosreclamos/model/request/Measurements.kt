package com.casadosreclamos.model.request

import javax.persistence.Embeddable

@Embeddable
class Measurements {
    var width: Double = 0.0
    var height: Double = 0.0
}
