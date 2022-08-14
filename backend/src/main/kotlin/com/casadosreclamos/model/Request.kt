package com.casadosreclamos.model

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

    @ManyToOne
    open lateinit var address: Address

    open lateinit var created: Date

    open lateinit var lastUpdate: Date

    @Enumerated(EnumType.STRING)
    open lateinit var status: RequestStatus

    open var trackingCode: String? =  null

    open var amount: Int = 0

    open var cost: Double = 0.0

    open var observations: String? = null

    open var application: Boolean = false

    @OneToOne
    open lateinit var type: RequestType
}