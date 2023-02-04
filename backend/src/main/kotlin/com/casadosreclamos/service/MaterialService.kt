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
        return materialRepository.streamAllWithFinishings()
    }

    fun getMaterials(materials: List<MaterialDto>?): Multi<Material> {
        return materialRepository.find(materials)
    }

    @Throws(InvalidNameException::class)
    fun add(materialDto: MaterialDto): Uni<Material> {
        if (materialDto.name.isNullOrBlank()) {
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
                        throw AlreadyExistsException("Material")
                    } else if (materialDto.mandatoryFinishings != null && groups.size < materialDto.mandatoryFinishings!!.size) {
                        throw InvalidIdException("finishing group")
                    } else if (materialDto.additionalFinishings != null && finishings.size < materialDto.additionalFinishings!!.size) {
                        throw InvalidIdException("finishing")
                    }

                    material.mandatoryFinishings = groups
                    material.additionalFinishings = finishings

                    materialRepository.persist(material)
                }
        }
    }

    @Throws(InvalidNameException::class)
    fun update(materialDto: MaterialDto): Uni<Material> {
        if (materialDto.id == null || materialDto.id!! <= 0) {
            throw InvalidIdException("material")
        } else if (materialDto.name.isNullOrBlank()) {
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
                    throw AlreadyExistsException("Material")
                }

                Uni.combine().all().unis(materialUni, finishingGroupUni, finishingsUni).asTuple().onItem()
                    .transform { tuple ->
                        val material = tuple.item1
                        val groups = tuple.item2
                        val finishings = tuple.item3

                        if (material == null) {
                            throw InvalidIdException("material")
                        } else if (materialDto.mandatoryFinishings != null && groups.size < materialDto.mandatoryFinishings!!.size) {
                            throw InvalidIdException("finishing group")
                        } else if (materialDto.additionalFinishings != null && finishings.size < materialDto.additionalFinishings!!.size) {
                            throw InvalidIdException("finishing")
                        }

                        material.name = materialDto.name!!
                        material.mandatoryFinishings = groups
                        material.additionalFinishings = finishings

                        material
                    }
            }
        }
    }

    @Throws(InvalidIdException::class)
    fun delete(id: Long): Uni<Response> {
        return Panache.withTransaction { materialRepository.deleteById(id) }.onItem()
            .transform { Response.ok().build() }.onFailure().transform { InvalidIdException("material") }
    }

    fun find(id: Long): Uni<Material> {
        return materialRepository.findById(id)
    }
}