package com.casadosreclamos.service

import com.casadosreclamos.dto.FinishingDto
import com.casadosreclamos.dto.NewFinishingDto
import com.casadosreclamos.exception.AlreadyExistsException
import com.casadosreclamos.exception.InvalidCostException
import com.casadosreclamos.exception.InvalidIdException
import com.casadosreclamos.model.request.Finishing
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
            finishingRepository.exists(finishingDto.name!!).onItem().transformToUni { value ->
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
}