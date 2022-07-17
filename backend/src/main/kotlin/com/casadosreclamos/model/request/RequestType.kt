package com.casadosreclamos.model.request

import com.casadosreclamos.dto.RequestTypeDto
import com.casadosreclamos.exception.InvalidRequestTypeException
import com.casadosreclamos.dto.OneFace as OneDto
import com.casadosreclamos.dto.TwoFaces as TwoDto
import com.casadosreclamos.dto.LeftShowcase as LeftDto
import com.casadosreclamos.dto.RightShowcase as RightDto
import io.smallrye.mutiny.Uni
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
    open lateinit var request: RequestInfo
}

@MappedSuperclass
abstract class Showcase: RequestType() {
    @ManyToOne
    open lateinit var top: Image
    @ManyToOne
    open lateinit var bottom: Image
    @ManyToOne
    open lateinit var left: Image
    @ManyToOne
    open lateinit var right: Image
    @ManyToOne
    open lateinit var side: Image
}

@Entity
@DiscriminatorValue("OneFace")
open class OneFace: RequestType() {
    @ManyToOne
    open lateinit var cover: Image
}

@Entity
@DiscriminatorValue("TwoFaces")
open class TwoFaces: RequestType() {
    @ManyToOne
    open lateinit var cover: Image

    @ManyToOne
    open lateinit var back: Image
}

@Entity
@DiscriminatorValue("LeftShowcase")
open class LeftShowcase: Showcase()

@Entity
@DiscriminatorValue("RightShowcase")
open class RightShowcase: Showcase()

private fun from(request: OneFace) {

}