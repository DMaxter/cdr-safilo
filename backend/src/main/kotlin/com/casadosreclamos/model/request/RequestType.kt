package com.casadosreclamos.model.request

import com.casadosreclamos.model.Request
import java.util.*
import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
abstract class RequestType {
    @Id
    open var id: Long = 0

    @OneToOne
    @JoinColumn(name = "id")
    @MapsId
    open lateinit var request: Request

    @Transient
    open var cost: Double = 0.0
}

@MappedSuperclass
abstract class Showcase: RequestType() {
    @ManyToOne
    open lateinit var top: RequestSlot
    @ManyToOne
    open lateinit var bottom: RequestSlot
    @ManyToOne
    open lateinit var left: RequestSlot
    @ManyToOne
    open lateinit var right: RequestSlot
    @ManyToOne
    open lateinit var side: RequestSlot
}

@Entity
@DiscriminatorValue("OneFace")
open class OneFace: RequestType() {
    @ManyToOne
    open lateinit var cover: RequestSlot
}

@Entity
@DiscriminatorValue("TwoFaces")
open class TwoFaces: RequestType() {
    @ManyToOne
    open lateinit var cover: RequestSlot

    @ManyToOne
    open lateinit var back: RequestSlot
}

@Entity
@DiscriminatorValue("LeftShowcase")
open class LeftShowcase: Showcase()

@Entity
@DiscriminatorValue("RightShowcase")
open class RightShowcase: Showcase()