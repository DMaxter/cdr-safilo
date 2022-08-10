package com.casadosreclamos.model.request

import com.casadosreclamos.model.Request
import javax.persistence.*

@Entity
open class RequestInfo {
    @Id
    open var id: Long = 0

    @OneToOne
    @JoinColumn(name = "id")
    @MapsId
    open lateinit var request: Request

    @OneToOne
    open lateinit var type: RequestType

    @ManyToOne
    open lateinit var brand: Brand

    @Embedded
    open lateinit var measurement: Measurements

    open var amount: Int = 0

    @ManyToOne
    open lateinit var material: Material
}