package com.casadosreclamos.model

import com.casadosreclamos.model.request.Brand
import java.io.Serializable
import javax.persistence.*

@Entity
open class Plafond {
    @EmbeddedId
    open lateinit var id: PlafondId

    open var amount: Double = 0.0

    @ManyToOne
    @MapsId("userId")
    open lateinit var user: User

    @ManyToOne
    @MapsId("brandId")
    open lateinit var brand: Brand
}

@Embeddable
class PlafondId : Serializable {
    lateinit var userId: String

    var brandId: Long = 0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlafondId

        if (userId != other.userId) return false
        if (brandId != other.brandId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = userId.hashCode()
        result = 31 * result + brandId.hashCode()
        return result
    }
}