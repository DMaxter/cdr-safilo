package com.casadosreclamos.model.request

import java.io.Serializable
import javax.persistence.*

@Entity
open class Price {
    @EmbeddedId
    open lateinit var id: PriceId

    open var cost: Double = 0.0

    @ManyToOne
    @MapsId("materialId")
    open lateinit var material: Material
}

@Embeddable
class PriceId : Serializable {
    @Embedded
    lateinit var measurements: Measurements

    var materialId: Long = 0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PriceId

        if (measurements != other.measurements) return false
        if (materialId != other.materialId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = measurements.hashCode()
        result = 31 * result + materialId.hashCode()
        return result
    }
}