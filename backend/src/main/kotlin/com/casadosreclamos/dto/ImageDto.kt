package com.casadosreclamos.dto

import com.casadosreclamos.model.request.Image

data class ImageDto(var id: Long?, var obsolete: Boolean?, var link: String?) {
    constructor(image: Image) : this(image.id, image.obsolete, image.link)
}
