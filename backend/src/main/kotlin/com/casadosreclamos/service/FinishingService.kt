package com.casadosreclamos.service

import com.casadosreclamos.dto.FinishingDto
import com.casadosreclamos.dto.FinishingGroupDto
import com.casadosreclamos.dto.NewFinishingDto
import com.casadosreclamos.exception.AlreadyExistsException
import com.casadosreclamos.exception.InvalidCostException
import com.casadosreclamos.exception.InvalidIdException
import com.casadosreclamos.model.request.Finishing
import com.casadosreclamos.model.request.FinishingGroup
import com.casadosreclamos.repo.FinishingGroupRepository
import com.casadosreclamos.repo.FinishingRepository
import io.quarkus.hibernate.reactive.panache.Panache
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import java.util.stream.Collectors
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.naming.InvalidNameException
import javax.ws.rs.core.Response
import kotlin.jvm.Throws

@ApplicationScoped
class FinishingService {
    @Inject
    lateinit var materialService: MaterialService

    @Inject
    lateinit var finishingRepository: FinishingRepository

    @Inject
    lateinit var finishingGroupRepository: FinishingGroupRepository

    fun getAll(): Multi<Finishing> {
        return finishingRepository.streamAll()
    }

    fun find(finishings: List<FinishingDto>): Multi<Finishing> {
        return finishingRepository.stream(finishings.stream().map { it.id!! }.collect(Collectors.toSet()))
    }

    fun find(finishings: Set<FinishingDto>): Multi<Finishing> {
        return finishingRepository.stream(finishings.stream().map { it.id!! }.collect(Collectors.toSet()))
    }

    @Throws(InvalidNameException::class, InvalidCostException::class, AlreadyExistsException::class)
    fun add(finishingDto: NewFinishingDto): Uni<Finishing> {
        if (finishingDto.name == null || finishingDto.name!!.isEmpty()) {
            throw InvalidNameException()
        } else if (finishingDto.cost == null || finishingDto.cost!! < 0) {
            throw InvalidCostException()
        }

        val finishing = Finishing()
        finishing.name = finishingDto.name!!

        return Panache.withTransaction {
            finishingRepository.exists(finishing.name).onItem().transformToUni { value ->
                return@transformToUni if (value) {
                    throw AlreadyExistsException("Finishing")
                } else {
                    finishingRepository.persist(finishing)
                }
            }.onItem().transformToUni { _ ->
                materialService.getMaterials(finishingDto.materials).onItem().transform { material ->
                    material.additionalFinishings.add(finishing)

                    material
                }.toUni().onItem().transform { _ ->
                    finishing
                }
            }
        }
    }

    @Throws(
        InvalidIdException::class,
        InvalidNameException::class,
        InvalidCostException::class,
        AlreadyExistsException::class
    )
    fun update(finishingDto: FinishingDto): Uni<Finishing> {
        if (finishingDto.id == null || finishingDto.id!! <= 0) {
            throw InvalidIdException("finishing")
        } else if (finishingDto.name == null || finishingDto.name!!.isEmpty()) {
            throw InvalidNameException()
        }

        return Panache.withTransaction {
            finishingRepository.exists(finishingDto.name!!, finishingDto.id!!).onItem().transformToUni { value ->
                if (value) {
                    throw AlreadyExistsException("Finishing")
                }

                finishingRepository.findById(finishingDto.id!!).onItem().transform { finishing ->
                    if (finishing == null) {
                        throw InvalidIdException("finishing")
                    }

                    finishing.name = finishingDto.name!!

                    finishing
                }
            }
        }
    }

    @Throws(InvalidIdException::class)
    fun delete(id: Long): Uni<Response> {
        return Panache.withTransaction {
            finishingRepository.deleteById(id).onItem().transform { Response.ok().build() }.onFailure()
                .transform { InvalidIdException("finishing") }
        }
    }

    fun getGroups(): Multi<FinishingGroup> {
        return finishingGroupRepository.streamAll()
    }

    @Throws(AlreadyExistsException::class, InvalidIdException::class, InvalidNameException::class)
    fun createGroup(name: String, finishings: Set<FinishingDto>): Uni<FinishingGroup> {
        if (name.isEmpty()) {
            throw InvalidNameException()
        }

        val group = FinishingGroup()
        group.name = name

        val finishingGroupUni = finishingGroupRepository.exists(name)
        val finishingsUni = if (finishings.isNotEmpty()) {
            find(finishings).collect().with(Collectors.toSet())
        } else {
            Uni.createFrom().item(
                setOf()
            )
        }

        return Panache.withTransaction {
            Uni.combine().all().unis(finishingGroupUni, finishingsUni).asTuple().onItem().transformToUni { tuple ->
                val exists = tuple.item1
                val finishingsFetched = tuple.item2

                if (exists) {
                    throw AlreadyExistsException("FinishingGroup")
                } else if (finishingsFetched.size < finishings.size) {
                    throw InvalidIdException("finishing")
                }

                group.finishings = finishingsFetched

                finishingGroupRepository.persist(group)
            }
        }
    }

    fun editGroup(group: FinishingGroupDto): Uni<FinishingGroup> {
        if (group.id == null || group.id!! <= 0) {
            throw InvalidIdException("finishing group")
        } else if (group.name.isNullOrEmpty()) {
            throw InvalidNameException()
        } else if (group.finishings == null) {
            group.finishings = setOf()
        }

        val existsUni = finishingGroupRepository.exists(group.name!!, group.id!!)
        val finishingsUni = if (group.finishings!!.isNotEmpty()) {
            find(group.finishings!!).collect().with(Collectors.toSet())
        } else {
            Uni.createFrom().item(
                setOf()
            )
        }
        val finishingGroupUni = finishingGroupRepository.findById(group.id!!)

        return Panache.withTransaction {
            Uni.combine().all().unis(existsUni, finishingsUni, finishingGroupUni).asTuple().onItem()
                .transform { tuple ->
                    val exists = tuple.item1
                    val finishings = tuple.item2
                    val finishingGroup = tuple.item3

                    if (exists) {
                        throw AlreadyExistsException("FinishingGroup")
                    } else if (finishings.size < group.finishings!!.size || finishingGroup == null) {
                        throw InvalidIdException("finishing")
                    }


                    finishingGroup.name = group.name!!
                    finishingGroup.finishings = finishings

                    finishingGroup
                }
        }
    }

    fun deleteGroup(id: Long): Uni<Response> {
        if (id <= 0) {
            throw InvalidIdException("finishing group")
        }

        return Panache.withTransaction {
            finishingGroupRepository.deleteById(id).onItem().transform { Response.ok().build() }.onFailure()
                .transform { InvalidIdException("finishing group") }
        }
    }
}