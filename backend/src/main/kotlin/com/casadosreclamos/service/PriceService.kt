package com.casadosreclamos.service

import com.casadosreclamos.dto.PriceDto
import com.casadosreclamos.exception.InvalidCostException
import com.casadosreclamos.exception.InvalidIdException
import com.casadosreclamos.exception.InvalidMeasurementsException
import com.casadosreclamos.exception.PriceNotFoundException
import com.casadosreclamos.model.request.Measurements
import com.casadosreclamos.model.request.Price
import com.casadosreclamos.model.request.PriceId
import com.casadosreclamos.repo.PriceRepository
import io.quarkus.hibernate.reactive.panache.Panache
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import org.jboss.logging.Logger
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class PriceService {
    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var priceRepository: PriceRepository

    @Inject
    lateinit var materialService: MaterialService

    fun getAll(): Multi<Price> {
        return priceRepository.streamAll()
    }

    fun get(measurements: Measurements, material: Long): Uni<Price> {
        if (measurements.height <= 0 || measurements.width <= 0) {
            throw InvalidMeasurementsException()
        } else if (material <= 0) {
            throw InvalidIdException("material")
        }

        val id = PriceId()
        id.measurements = measurements
        id.materialId = material

        return Panache.withTransaction {
            priceRepository.findById(id)
        }
    }

    fun add(priceDto: PriceDto): Uni<Price> {
        if (priceDto.width == null || priceDto.width!! <= 0 || priceDto.height == null || priceDto.height!! <= 0) {
            throw InvalidMeasurementsException()
        } else if (priceDto.cost == null || priceDto.cost!! <= 0) {
            throw InvalidCostException()
        } else if (priceDto.material == null || priceDto.material!! <= 0) {
            throw InvalidIdException("material")
        }

        return materialService.find(priceDto.material!!).onItem().transformToUni { material ->
            if (material == null) {
                throw InvalidIdException("material")
            }

            val priceId = PriceId()
            priceId.measurements = Measurements()
            priceId.measurements.height = priceDto.height!!
            priceId.measurements.width = priceDto.width!!
            priceId.materialId = material.id

            val price = Price()
            price.id = priceId
            price.cost = priceDto.cost!!
            price.material = material

            Panache.withTransaction {
                priceRepository.persist(price)
            }
        }
    }

    fun edit(priceDto: PriceDto): Uni<Price> {
        if (priceDto.width == null || priceDto.width!! <= 0 || priceDto.height == null || priceDto.height!! <= 0) {
            throw InvalidMeasurementsException()
        } else if (priceDto.cost == null || priceDto.cost!! <= 0) {
            throw InvalidCostException()
        } else if (priceDto.material == null || priceDto.material!! <= 0) {
            throw InvalidIdException("material")
        }

        val id = PriceId()
        id.measurements = Measurements()
        id.measurements.height = priceDto.height!!
        id.measurements.width = priceDto.width!!
        id.materialId = priceDto.material!!

        return Panache.withTransaction {
            priceRepository.findById(id).onItem().transform { price ->
                if (price == null) {
                    throw PriceNotFoundException()
                }

                price.cost = priceDto.cost!!

                return@transform price
            }
        }
    }

    fun delete(priceDto: PriceDto): Uni<Void> {
        if (priceDto.width == null || priceDto.width!! <= 0 || priceDto.height == null || priceDto.height!! <= 0) {
            throw InvalidMeasurementsException()
        } else if (priceDto.material == null || priceDto.material!! <= 0) {
            throw InvalidIdException("material")
        }

        val id = PriceId()
        id.measurements = Measurements()
        id.measurements.height = priceDto.height!!
        id.measurements.width = priceDto.width!!
        id.materialId = priceDto.material!!

        return Panache.withTransaction {
            priceRepository.deleteById(id).onItem().transformToUni { deleted ->
                if (!deleted) {
                    throw PriceNotFoundException()
                }

                return@transformToUni Uni.createFrom().voidItem()
            }
        }
    }
}