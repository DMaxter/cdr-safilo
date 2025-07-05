package com.casadosreclamos.service

import com.casadosreclamos.dto.FinishingDto
import com.casadosreclamos.dto.PriceDto
import com.casadosreclamos.exception.InvalidCostException
import com.casadosreclamos.exception.InvalidIdException
import com.casadosreclamos.exception.MissingPriceException
import com.casadosreclamos.model.request.Price
import com.casadosreclamos.repo.PriceRepository
import io.quarkus.hibernate.reactive.panache.Panache
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import java.util.stream.Collectors
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import org.jboss.logging.Logger

@ApplicationScoped
class PriceService {
    @Inject lateinit var logger: Logger

    @Inject lateinit var priceRepository: PriceRepository

    @Inject lateinit var materialService: MaterialService

    @Inject lateinit var finishingService: FinishingService

    fun getAll(): Multi<Price> {
        return priceRepository.streamAll()
    }

    fun get(material: Long, finishings: Set<FinishingDto>): Uni<Price> {
        val ids = finishings.stream().map { it.id!! }.collect(Collectors.toSet())

        val validPrices =
                priceRepository
                        .stream(material)
                        .select()
                        .`when` { price ->
                            Uni.createFrom()
                                    .item(
                                            price.finishings
                                                    .stream()
                                                    .map { it.id }
                                                    .collect(Collectors.toSet())
                                                    .containsAll(ids)
                                    )
                        }
                        .collect()
                        .asList()

        return validPrices.onItem().transform { items ->
            if (items.isEmpty()) {
                logger.error(
                        "Couldn't find a suitable price for material ${material} and finishings ${finishings}"
                )

                throw MissingPriceException()
            } else if (items.size == 1) {
                return@transform items[0]
            } else {
                // Check for prices that have the same number of finishings
                var tmpItems =
                        items.stream()
                                .filter { it.finishings.size == ids.size }
                                .collect(Collectors.toList())

                if (tmpItems.size == 1) {
                    return@transform tmpItems[0]
                } else if (tmpItems.isEmpty()) {
                    // If no price with same number found, then reset and choose the cheapest one
                    tmpItems = items
                }

                logger.info("Found multiple prices: $tmpItems")

                return@transform tmpItems.sortedBy { it.costPerSquareMeter }[0]
            }
        }
    }

    fun add(priceDto: PriceDto): Uni<Price> {
        logger.info("Registering price $priceDto")

        if (priceDto.costPerSquareMeter == null || priceDto.costPerSquareMeter!! <= 0) {
            logger.error("Price cost per square meter is invalid")

            throw InvalidCostException()
        } else if (priceDto.fixedCost != null && priceDto.fixedCost!! < 0) {
            logger.error("Price fixed cost is invalid")

            throw InvalidCostException()
        } else if (priceDto.material == null || priceDto.material!! <= 0) {
            logger.error("Price material ID is invalid")

            throw InvalidIdException("material")
        } else if (priceDto.finishings == null ||
                        priceDto.finishings!!
                                .stream()
                                .filter { it?.id == null || it.id!! <= 0 }
                                .count() > 0
        ) {
            logger.error("At least one price finishing has invalid ID")

            throw InvalidIdException("acabamento")
        }

        val materialUni = materialService.find(priceDto.material!!)
        val finishingUni =
                if (priceDto.finishings!!.isNotEmpty()) {
                    finishingService
                            .find(priceDto.finishings!!)
                            .filter { finishing -> !finishing.obsolete }
                            .collect()
                            .with(Collectors.toSet())
                } else {
                    Uni.createFrom().item(setOf())
                }

        return Panache.withTransaction {
            Uni.combine().all().unis(materialUni, finishingUni).asTuple().onItem().transformToUni {
                    tuple ->
                val material = tuple.item1
                val finishings = tuple.item2

                if (material == null) {
                    logger.error("Material with ID ${priceDto.material} is not registered")

                    throw InvalidIdException("material")
                } else if ((finishings == null && priceDto.finishings!!.isNotEmpty()) ||
                                (finishings.size < priceDto.finishings!!.size)
                ) {
                    logger.error("Invalid or repeated finishings detected. Fetched: $finishings")

                    throw InvalidIdException("acabamento")
                }

                val price = Price()
                price.costPerSquareMeter = priceDto.costPerSquareMeter!!
                price.fixedCost = priceDto.fixedCost!!
                price.finishings = finishings
                price.material = material

                priceRepository
                        .persist(price)
                        .onItem()
                        .invoke { _ -> logger.info("Successfully created price") }
                        .onFailure()
                        .invoke { e -> logger.error("Couldn't register price: $e") }
            }
        }
    }

    fun edit(priceDto: PriceDto): Uni<Price> {
        logger.info("Updating price $priceDto")

        if (priceDto.id == null || priceDto.id!! <= 0) {
            logger.error("Price ID is invalid")

            throw InvalidIdException("preço")
        } else if (priceDto.costPerSquareMeter == null || priceDto.costPerSquareMeter!! <= 0) {
            logger.error("Price cost per square meter is invalid")

            throw InvalidCostException()
        } else if (priceDto.fixedCost == null || priceDto.fixedCost!! <= 0) {
            logger.error("Price fixed cost is invalid")

            throw InvalidCostException()
        } else if (priceDto.material == null || priceDto.material!! <= 0) {
            logger.error("Price material ID is invalid")

            throw InvalidIdException("material")
        } else if (priceDto.finishings == null ||
                        priceDto.finishings!!
                                .stream()
                                .filter { it?.id == null || it.id!! <= 0 }
                                .count() > 0
        ) {
            logger.error("At least one price finishing has invalid ID")

            throw InvalidIdException("acabamento")
        }

        return Panache.withTransaction {
            priceRepository.findById(priceDto.id!!).onItem().transformToUni { price ->
                if (price == null) {
                    throw InvalidIdException("preço")
                }

                val materialUni = materialService.find(priceDto.material!!)
                val finishingUni =
                        if (priceDto.finishings!!.isNotEmpty()) {
                            finishingService
                                    .find(priceDto.finishings!!)
                                    .collect()
                                    .with(Collectors.toSet())
                        } else {
                            Uni.createFrom().item(setOf())
                        }

                return@transformToUni Uni.combine()
                        .all()
                        .unis(materialUni, finishingUni)
                        .asTuple()
                        .onItem()
                        .transform { tuple ->
                            val material = tuple.item1
                            val finishings = tuple.item2

                            if (material == null) {
                                logger.error(
                                        "Material with ID ${priceDto.material} is not registered"
                                )

                                throw InvalidIdException("material")
                            } else if ((finishings == null && priceDto.finishings!!.isNotEmpty()) ||
                                            (finishings.size < priceDto.finishings!!.size)
                            ) {
                                logger.error(
                                        "Invalid or repeated finishings detected. Fetched: $finishings"
                                )

                                throw InvalidIdException("acabamento")
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
            priceRepository
                    .deleteById(id)
                    .onItem()
                    .transformToUni { deleted ->
                        if (!deleted) {
                            logger.error("Price with ID $id is not registered")

                            throw InvalidIdException("preço")
                        }

                        logger.info("Successfully deleted price")

                        return@transformToUni Uni.createFrom().voidItem()
                    }
                    .onFailure()
                    .invoke { e -> logger.error("Couldn't delete price: $e") }
        }
    }
}
