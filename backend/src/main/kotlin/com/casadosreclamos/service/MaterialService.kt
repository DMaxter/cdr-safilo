package com.casadosreclamos.service

import com.casadosreclamos.dto.MaterialDto
import com.casadosreclamos.exception.AlreadyExistsException
import com.casadosreclamos.exception.InvalidCostException
import com.casadosreclamos.exception.InvalidIdException
import com.casadosreclamos.exception.InvalidNameException
import com.casadosreclamos.model.request.FinishingGroup
import com.casadosreclamos.model.request.Material
import com.casadosreclamos.repo.MaterialRepository
import io.quarkus.hibernate.reactive.panache.Panache
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import org.jboss.logging.Logger
import java.util.stream.Collectors
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.core.Response

@ApplicationScoped
class MaterialService {
    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var materialRepository: MaterialRepository

    @Inject
    lateinit var finishingService: FinishingService

    fun getAll(): Multi<Material> {
        return materialRepository.streamAll()
    }

    fun getMaterials(materials: List<MaterialDto>?): Multi<Material> {
        return materialRepository.find(materials)
    }

    @Throws(InvalidNameException::class)
    fun add(materialDto: MaterialDto): Uni<Material> {
        logger.info("Registering material $materialDto")

        if (materialDto.name.isNullOrBlank()) {
            logger.error("Material name is null or empty")

            throw InvalidNameException()
        }

        val material = Material()
        material.name = materialDto.name!!

        val materialExistsUni = materialRepository.exists(material.name)
        val finishingGroupUni = if (materialDto.mandatoryFinishings.isNullOrEmpty()) {
            Uni.createFrom().item(setOf())
        } else {
            finishingService.findGroups(materialDto.mandatoryFinishings!!).collect().with(Collectors.toSet())
        }

        val finishingsUni = if (materialDto.additionalFinishings.isNullOrEmpty()) {
            Uni.createFrom().item(setOf())
        } else {
            finishingService.find(materialDto.additionalFinishings!!).collect().with(Collectors.toSet())
        }

        return Panache.withTransaction {
            Uni.combine().all().unis(materialExistsUni, finishingGroupUni, finishingsUni).asTuple().onItem()
                .transformToUni { tuple ->
                    val exists = tuple.item1
                    val groups = tuple.item2
                    val finishings = tuple.item3

                    if (exists) {
                        logger.error("A material with name ${materialDto.name} is already registered")

                        throw AlreadyExistsException("Material")
                    } else if (materialDto.mandatoryFinishings != null && groups.size < materialDto.mandatoryFinishings!!.size) {
                        logger.error("Invalid or repeated finishing groups detected. Fetched: $groups")

                        throw InvalidIdException("finishing group")
                    } else if (materialDto.additionalFinishings != null && finishings.size < materialDto.additionalFinishings!!.size) {
                        logger.error("Invalid or repeated additional finishings detected. Fetched: $finishings")

                        throw InvalidIdException("finishing")
                    }

                    material.mandatoryFinishings = groups
                    material.additionalFinishings = finishings

                    materialRepository.persist(material).onItem()
                        .invoke { _ -> logger.info("Successfully created material") }.onFailure()
                        .invoke { e -> logger.error("Couldn't register material: $e") }
                }
        }
    }

    @Throws(InvalidNameException::class)
    fun update(materialDto: MaterialDto): Uni<Material> {
        logger.info("Updating material $materialDto")

        if (materialDto.id == null || materialDto.id!! <= 0) {
            logger.error("Material ID is invalid")

            throw InvalidIdException("material")
        } else if (materialDto.name.isNullOrBlank()) {
            logger.error("Material name is null or empty")

            throw InvalidNameException()
        }

        val materialUni = materialRepository.findById(materialDto.id!!)
        val finishingGroupUni = if (materialDto.mandatoryFinishings.isNullOrEmpty()) {
            Uni.createFrom().item(setOf())
        } else {
            finishingService.findGroups(materialDto.mandatoryFinishings!!).collect().with(Collectors.toSet())
        }

        val finishingsUni = if (materialDto.additionalFinishings.isNullOrEmpty()) {
            Uni.createFrom().item(setOf())
        } else {
            finishingService.find(materialDto.additionalFinishings!!).collect().with(Collectors.toSet())
        }

        return Panache.withTransaction {
            materialRepository.exists(materialDto.name!!, materialDto.id!!).onItem().transformToUni { exists ->
                if (exists) {
                    logger.error("A material with name ${materialDto.name} is already registered")

                    throw AlreadyExistsException("Material")
                }

                Uni.combine().all().unis(materialUni, finishingGroupUni, finishingsUni).asTuple().onItem()
                    .transform { tuple ->
                        val material = tuple.item1
                        val groups = tuple.item2
                        val finishings = tuple.item3

                        if (material == null) {
                            logger.error("Material with ID ${materialDto.id} is not registered")

                            throw InvalidIdException("material")
                        } else if (materialDto.mandatoryFinishings != null && groups.size < materialDto.mandatoryFinishings!!.size) {
                            logger.error("Invalid or repeated finishing groups detected. Fetched: $groups")

                            throw InvalidIdException("finishing group")
                        } else if (materialDto.additionalFinishings != null && finishings.size < materialDto.additionalFinishings!!.size) {
                            logger.error("Invalid or repeated additional finishings detected. Fetched: $finishings")

                            throw InvalidIdException("finishing")
                        }

                        material.name = materialDto.name!!
                        material.mandatoryFinishings = groups
                        material.additionalFinishings = finishings

                        logger.info("Successfully updated material")

                        material
                    }
            }
        }
    }

    @Throws(InvalidIdException::class)
    fun delete(id: Long): Uni<Response> {
        return Panache.withTransaction { materialRepository.deleteById(id) }.onItem().transform { _ ->
                logger.info("Successfully deleted material")

                Response.ok().build()
            }.onFailure().transform { e ->
                logger.info("Couldn't delete material: $e")

                InvalidIdException("material")
            }
    }

    fun find(id: Long): Uni<Material> {
        return materialRepository.findById(id)
    }
}