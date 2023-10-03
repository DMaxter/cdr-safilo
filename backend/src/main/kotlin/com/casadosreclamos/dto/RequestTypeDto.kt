package com.casadosreclamos.dto

import com.casadosreclamos.exception.InvalidRequestTypeException
import com.casadosreclamos.model.request.RequestType
import com.casadosreclamos.model.request.OneFace as OneFaceRequest
import com.casadosreclamos.model.request.TwoFaces as TwoFacesRequest
import com.casadosreclamos.model.request.LeftShowcase as LeftRequest
import com.casadosreclamos.model.request.RightShowcase as RightRequest
import com.casadosreclamos.model.request.SimpleShowcase as SimpleRequest
import com.casadosreclamos.model.request.Showcase
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType
import org.eclipse.microprofile.openapi.annotations.media.Schema

private const val PROPERTY = "type"

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = PROPERTY)
@JsonSubTypes(
    JsonSubTypes.Type(OneFace::class, name = "OneFace"),
    JsonSubTypes.Type(TwoFaces::class, name = "TwoFaces"),
    JsonSubTypes.Type(SimpleShowcase::class, name = "SimpleShowcase"),
    JsonSubTypes.Type(LeftShowcase::class, name = "LeftShowcase"),
    JsonSubTypes.Type(RightShowcase::class, name = "RightShowcase")
)
@Schema(
    type = SchemaType.OBJECT,
    discriminatorProperty = PROPERTY,
    oneOf = [OneFace::class, TwoFaces::class, SimpleShowcase::class, LeftShowcase::class, RightShowcase::class]
)
abstract class RequestTypeDto {
    companion object {
        fun from(request: RequestType): RequestTypeDto {
            return when (request) {
                is OneFaceRequest -> fromOneFace(request)
                is TwoFacesRequest -> fromTwoFaces(request)
                is SimpleRequest -> fromSimpleShowcase(request)
                is LeftRequest -> fromLeftShowcase(request)
                is RightRequest -> fromRightShowcase(request)
                else -> throw InvalidRequestTypeException()
            }
        }
    }
}

abstract class ShowcaseDto: RequestTypeDto() {
    lateinit var top: RequestSlotDto
    lateinit var bottom: RequestSlotDto
    lateinit var left: RequestSlotDto
    lateinit var right: RequestSlotDto
    lateinit var side: RequestSlotDto

    fun fromRequest(request: Showcase) {
        this.top = RequestSlotDto(request.top)
        this.bottom = RequestSlotDto(request.bottom)
        this.left = RequestSlotDto(request.left)
        this.right = RequestSlotDto(request.right)
        this.side = RequestSlotDto(request.side)
    }

    override fun toString(): String {
        return "TOP: ($top), BOTTOM: ($bottom), LEFT: ($left), RIGHT: ($right), SIDE: ($side)"
    }
}

class OneFace() : RequestTypeDto() {
    lateinit var cover: RequestSlotDto

    constructor(request: OneFaceRequest) : this() {
        this.cover = RequestSlotDto(request.cover)
    }

    override fun toString(): String {
        return "FACE: $cover"
    }
}

class TwoFaces() : RequestTypeDto() {
    lateinit var cover: RequestSlotDto

    lateinit var back: RequestSlotDto


    constructor(request: TwoFacesRequest) : this() {
        this.cover = RequestSlotDto(request.cover)
        this.back = RequestSlotDto(request.back)
    }

    override fun toString(): String {
        return "FRONT: $cover, BACK: $back"
    }
}

class SimpleShowcase(): RequestTypeDto() {
    lateinit var top: RequestSlotDto
    lateinit var bottom: RequestSlotDto
    lateinit var left: RequestSlotDto
    lateinit var right: RequestSlotDto

    constructor(request: SimpleRequest): this() {
        this.top = RequestSlotDto(request.top)
        this.bottom = RequestSlotDto(request.bottom)
        this.left = RequestSlotDto(request.left)
        this.right = RequestSlotDto(request.right)
    }
}

class LeftShowcase() : ShowcaseDto() {
    constructor(request: LeftRequest) : this() {
        this.fromRequest(request)
    }
}
class RightShowcase() : ShowcaseDto() {
    constructor(request: RightRequest) : this() {
        this.fromRequest(request)
    }
}

private fun fromOneFace(request: OneFaceRequest): OneFace {
    return OneFace(request)
}

private fun fromTwoFaces(request: TwoFacesRequest): TwoFaces {
    return TwoFaces(request)
}

private fun fromSimpleShowcase(request: SimpleRequest): SimpleShowcase {
    return SimpleShowcase(request)
}

private fun fromLeftShowcase(request: LeftRequest): LeftShowcase {
    return LeftShowcase(request)
}

private fun fromRightShowcase(request: RightRequest): RightShowcase {
    return RightShowcase(request)
}
