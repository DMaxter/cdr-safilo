package com.casadosreclamos.service

import com.casadosreclamos.dto.FinishingDto
import com.casadosreclamos.dto.PriceDto
import com.casadosreclamos.exception.InvalidCostException
import com.casadosreclamos.exception.InvalidIdException
import com.casadosreclamos.exception.MissingPriceException
import com.casadosreclamos.exception.PriceNotFoundException
import com.casadosreclamos.model.request.Finishing
import com.casadosreclamos.model.request.Price
import com.casadosreclamos.repo.PriceRepository
import io.quarkus.hibernate.reactive.panache.Panache
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import org.jboss.logging.Logger
import java.util.stream.Collectors
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

    @Inject
    lateinit var finishingService: FinishingService

    fun getAll(): Multi<Price> {
        return priceRepository.streamAll()
    }

    fun get(material: Long, finishings: Set<FinishingDto>): Uni<Price> {
        val ids = finishings.stream().map { it.id!! }.collect(Collectors.toSet())

        val validPrices = priceRepository.stream(material).select().`when` { price ->
            Uni.createFrom().item(price.finishings.stream().map { it.id }.collect(Collectors.toSet()).containsAll(ids))
        }.collect().asList()

        return validPrices.onItem().transform { items ->
            if (items.isEmpty()) {
                throw MissingPriceException()
            } else if (items.size == 1) {
                return@transform items[0]
            } else {
                var tmpItems = items.stream().filter { it.finishings.size == ids.size }.collect(Collectors.toList())

                if (tmpItems.size == 1) {
                    return@transform tmpItems[0]
                } else if (tmpItems.isEmpty()) {
                    tmpItems = items
                }

                return@transform tmpItems.sortedBy { it.fixedCost }[0]
            }
        }
    }

    fun add(priceDto: PriceDto): Uni<Price> {
        if (priceDto.costPerSquareMeter == null || priceDto.costPerSquareMeter!! <= 0) {
            throw InvalidCostException()
        } else if (priceDto.fixedCost != null && priceDto.fixedCost!! < 0) {
            throw InvalidCostException()
        } else if (priceDto.material == null || priceDto.material!! <= 0) {
            throw InvalidIdException("material")
        } else if (priceDto.finishings == null || priceDto.finishings!!.stream()
                .filter { it?.id == null || it.id!! <= 0 }.count() > 0
        ) {
            throw InvalidIdException("finishing")
        }

        val materialUni = materialService.find(priceDto.material!!)
        val finishingUni = if (priceDto.finishings!!.isNotEmpty()) {
            finishingService.find(priceDto.finishings!!).collect().with(Collectors.toSet())
        } else {
            Uni.createFrom().item(setOf())
        }

        return Panache.withTransaction {
            Uni.combine().all().unis(materialUni, finishingUni).asTuple().onItem().transformToUni { tuple ->
                val material = tuple.item1
                val finishings = tuple.item2

                if (material == null) {
                    throw InvalidIdException("material")
                } else if ((finishings == null && priceDto.finishings!!.isNotEmpty()) || (finishings.size < priceDto.finishings!!.size)) {
                    throw InvalidIdException("finishing")
                }

                val price = Price()
                price.costPerSquareMeter = priceDto.costPerSquareMeter!!
                price.fixedCost = priceDto.fixedCost!!
                price.finishings = finishings
                price.material = material

                priceRepository.persist(price)
            }
        }
    }

    fun edit(priceDto: PriceDto): Uni<Price> {
        if (priceDto.id == null || priceDto.id!! <= 0) {
            throw InvalidIdException("price")
        } else if (priceDto.costPerSquareMeter == null || priceDto.costPerSquareMeter!! <= 0) {
            throw InvalidCostException()
        } else if (priceDto.fixedCost == null || priceDto.fixedCost!! <= 0) {
            throw InvalidCostException()
        } else if (priceDto.material == null || priceDto.material!! <= 0) {
            throw InvalidIdException("material")
        } else if (priceDto.finishings == null || priceDto.finishings!!.stream()
                .filter { it?.id == null || it.id!! <= 0 }.count() > 0
        ) {
            throw InvalidIdException("finishing")
        }

        return Panache.withTransaction {
            priceRepository.findById(priceDto.id!!).onItem().transformToUni { price ->
                if (price == null) {
                    throw PriceNotFoundException()
                }

                val materialUni = materialService.find(priceDto.material!!)
                val finishingUni = if (priceDto.finishings!!.isNotEmpty()) {
                    finishingService.find(priceDto.finishings!!).collect().with(Collectors.toSet())
                } else {
                    Uni.createFrom().item(setOf())
                }

                return@transformToUni Uni.combine().all().unis(materialUni, finishingUni).asTuple().onItem()
                    .transform { tuple ->
                        val material = tuple.item1
                        val finishings = tuple.item2

                        if (material == null) {
                            throw InvalidIdException("material")
                        } else if ((finishings == null && priceDto.finishings!!.isNotEmpty()) || (finishings.size < priceDto.finishings!!.size)) {
                            throw InvalidIdException("finishing")
                        }

                        price.costPerSquareMeter = priceDto.costPerSquareMeter!!
                        price.fixedCost = priceDto.fixedCost!!
                        price.finishings = finishings
                        price.material = material

                        return@transform price
                    }
            }
        }
    }

    fun delete(id: Long): Uni<Void> {
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