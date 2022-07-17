package com.casadosreclamos.dto

import com.casadosreclamos.exception.InvalidRequestTypeException
import com.casadosreclamos.model.request.RequestType
import com.casadosreclamos.model.request.OneFace as OneFaceRequest
import com.casadosreclamos.model.request.TwoFaces as TwoFacesRequest
import com.casadosreclamos.model.request.LeftShowcase as LeftRequest
import com.casadosreclamos.model.request.RightShowcase as RightRequest
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
    JsonSubTypes.Type(LeftShowcase::class, name = "LeftShowcase"),
    JsonSubTypes.Type(RightShowcase::class, name = "RightShowcase")
)
@Schema(
    type = SchemaType.OBJECT,
    discriminatorProperty = PROPERTY,
    oneOf = [OneFace::class, TwoFaces::class, LeftShowcase::class, RightShowcase::class]
)
abstract class RequestTypeDto() {
    companion object {
        fun from(request: RequestType): RequestTypeDto {
            return when (request) {
                is OneFaceRequest -> fromOneFace(request)
                is TwoFacesRequest -> fromTwoFaces(request)
                is LeftRequest -> fromLeftShowcase(request)
                is RightRequest -> fromRightShowcase(request)
                else -> throw InvalidRequestTypeException()
            }
        }
    }
}

abstract class ShowcaseDto() : RequestTypeDto() {
    lateinit var top: ImageDto
    lateinit var bottom: ImageDto
    lateinit var left: ImageDto
    lateinit var right: ImageDto
    lateinit var side: ImageDto

    constructor(request: Showcase) : this() {
        this.top = ImageDto(request.top)
        this.bottom = ImageDto(request.bottom)
        this.left = ImageDto(request.left)
        this.right = ImageDto(request.right)
        this.side = ImageDto(request.side)
    }
}

class OneFace() : RequestTypeDto() {
    lateinit var cover: ImageDto

    constructor(request: OneFaceRequest) : this() {
        this.cover = ImageDto(request.cover)
    }
}

class TwoFaces() : RequestTypeDto() {
    lateinit var cover: ImageDto

    lateinit var back: ImageDto


    constructor(request: TwoFacesRequest) : this() {
        this.cover = ImageDto(request.cover)
        this.back = ImageDto(request.back)
    }
}

class LeftShowcase(request: LeftRequest) : ShowcaseDto(request)
class RightShowcase(request: RightRequest) : ShowcaseDto(request)

private fun fromOneFace(request: OneFaceRequest): OneFace {
    return OneFace(request)
}

private fun fromTwoFaces(request: TwoFacesRequest): TwoFaces {
    return TwoFaces(request)
}

private fun fromLeftShowcase(request: LeftRequest): LeftShowcase {
    return LeftShowcase(request)
}

private fun fromRightShowcase(request: RightRequest): RightShowcase {
    return RightShowcase(request)
}
