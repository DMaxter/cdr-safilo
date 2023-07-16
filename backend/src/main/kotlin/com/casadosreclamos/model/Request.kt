package com.casadosreclamos.model

import com.casadosreclamos.model.request.Brand
import com.casadosreclamos.model.request.RequestStatus
import com.casadosreclamos.model.request.RequestType
import java.util.*
import javax.persistence.*

@Entity
open class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long = 0

    @ManyToOne
    open lateinit var requester: User

    @ManyToOne
    open lateinit var client: Client

    open lateinit var created: Date

    open lateinit var lastUpdate: Date

    @Enumerated(EnumType.STRING)
    open lateinit var status: RequestStatus

    open var trackingCode: Long? =  null

    @ManyToOne
    open lateinit var brand: Brand

    open var amount: Int = 0

    open var cost: Double? = null

    open var observations: String? = null

    open var application: Boolean = false

    @OneToOne
    open lateinit var type: RequestType
}