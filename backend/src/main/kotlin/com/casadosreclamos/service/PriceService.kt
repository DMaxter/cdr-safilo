package com.casadosreclamos.service

import com.casadosreclamos.dto.PriceDto
import com.casadosreclamos.exception.InvalidCostException
import com.casadosreclamos.exception.InvalidMeasurementsException
import com.casadosreclamos.exception.PriceNotFoundException
import com.casadosreclamos.model.request.Measurements
import com.casadosreclamos.model.request.Price
import com.casadosreclamos.repo.PriceRepository
import io.quarkus.hibernate.reactive.panache.Panache
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

    fun getPrice(measurements: Measurements): Uni<Price> {
        if (measurements.height <= 0 || measurements.width <= 0) {
            throw InvalidMeasurementsException()
        }

        return Panache.withTransaction {
            priceRepository.findById(measurements)
        }
    }

    fun add(priceDto: PriceDto): Uni<Price> {
        if (priceDto.width == null || priceDto.width!! <= 0 || priceDto.height == null || priceDto.height!! <= 0) {
            throw InvalidMeasurementsException()
        } else if (priceDto.cost == null || priceDto.cost!! <= 0) {
            throw InvalidCostException()
        }

        val price = Price()
        price.measurements = Measurements()
        price.measurements.height = priceDto.height!!
        price.measurements.width = priceDto.width!!
        price.cost = priceDto.cost!!

        return Panache.withTransaction {
            priceRepository.persist(price)
        }
    }

    fun edit(priceDto: PriceDto): Uni<Price> {
        if (priceDto.width == null || priceDto.width!! <= 0 || priceDto.height == null || priceDto.height!! <= 0) {
            throw InvalidMeasurementsException()
        } else if (priceDto.cost == null || priceDto.cost!! <= 0) {
            throw InvalidCostException()
        }

        val measurements = Measurements()
        measurements.height = priceDto.height!!
        measurements.width = priceDto.width!!

        return Panache.withTransaction {
            priceRepository.findById(measurements).onItem().transform { price ->
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
        }

        val measurements = Measurements()
        measurements.height = priceDto.height!!
        measurements.width = priceDto.width!!

        return Panache.withTransaction {
            priceRepository.deleteById(measurements).onItem().transformToUni { deleted ->
                if (!deleted) {
                    throw PriceNotFoundException()
                }

                return@transformToUni Uni.createFrom().voidItem()
            }
        }
    }
}