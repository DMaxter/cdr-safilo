package com.casadosreclamos.model

import com.casadosreclamos.model.request.RequestInfo
import com.casadosreclamos.model.request.RequestStatus
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

    open var trackingCode: String? =  null

    @OneToOne(mappedBy = "request")
    open lateinit var info: RequestInfo

    open var cost: Double = 0.0
}