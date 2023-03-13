package com.casadosreclamos.dto

import com.casadosreclamos.model.request.Brand

data class BrandDto(var id: Long?, var name: String?, var images: List<ImageDto>?) {
    constructor(brand: Brand) : this(brand.id, brand.name, brand.images.stream().map { ImageDto(it) }.toList())
}