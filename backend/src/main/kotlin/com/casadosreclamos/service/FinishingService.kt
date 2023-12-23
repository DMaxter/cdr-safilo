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
import org.jboss.logging.Logger

@ApplicationScoped
class FinishingService {
    @Inject lateinit var logger: Logger

    @Inject lateinit var materialService: MaterialService

    @Inject lateinit var finishingRepository: FinishingRepository

    @Inject lateinit var finishingGroupRepository: FinishingGroupRepository

    fun getAll(): Multi<Finishing> {
        return finishingRepository.streamAll()
    }

    fun find(finishings: Set<FinishingDto>): Multi<Finishing> {
        return finishingRepository.stream(
                finishings.stream().map { it.id!! }.collect(Collectors.toSet())
        )
    }

    @Throws(InvalidNameException::class, InvalidCostException::class, AlreadyExistsException::class)
    fun add(finishingDto: NewFinishingDto): Uni<Finishing> {
        logger.info("Registering finishing $finishingDto")

        if (finishingDto.name == null || finishingDto.name!!.isEmpty()) {
            logger.error("Finishing name is null or empty")

            throw InvalidNameException()
        } else if (finishingDto.cost == null || finishingDto.cost!! < 0) {
            logger.error("Finishing cost is invalid")

            throw InvalidCostException()
        }

        val finishing = Finishing()
        finishing.name = finishingDto.name!!

        return Panache.withTransaction {
            finishingRepository
                    .exists(finishing.name)
                    .onItem()
                    .transformToUni { value ->
                        return@transformToUni if (value) {
                            logger.error(
                                    "A finishing with name ${finishing.name} is already registered"
                            )

                            throw AlreadyExistsException("Finishing")
                        } else {
                            finishingRepository
                                    .persist(finishing)
                                    .onItem()
                                    .invoke { _ ->
                                        logger.info("Successfully registered finishing")
                                    }
                                    .onFailure()
                                    .invoke { e -> logger.error("Couldn't register finishing: $e") }
                        }
                    }
                    .onItem()
                    .transformToUni { _ ->
                        materialService
                                .getMaterials(finishingDto.materials)
                                .onItem()
                                .transform { material ->
                                    logger.info("Adding as additional to material ${material.name}")

                                    material.additionalFinishings.add(finishing)

                                    material
                                }
                                .toUni()
                                .onItem()
                                .transform { _ -> finishing }
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
        logger.info("Updating finishing $finishingDto")

        if (finishingDto.id == null || finishingDto.id!! <= 0) {
            logger.error("Finishing ID is invalid")

            throw InvalidIdException("finishing")
        } else if (finishingDto.name == null || finishingDto.name!!.isEmpty()) {
            logger.error("Finishing name is null or empty")

            throw InvalidNameException()
        }

        return Panache.withTransaction {
            finishingRepository
                    .exists(finishingDto.name!!, finishingDto.id!!)
                    .onItem()
                    .transformToUni { value ->
                        if (value) {
                            logger.error(
                                    "A finishing with name ${finishingDto.name} is already registered"
                            )

                            throw AlreadyExistsException("Finishing")
                        }

                        finishingRepository.findById(finishingDto.id!!).onItem().transform {
                                finishing ->
                            if (finishing == null) {
                                logger.error(
                                        "Finishing with ID ${finishingDto.id} is not registered"
                                )

                                throw InvalidIdException("finishing")
                            }

                            finishing.name = finishingDto.name!!
                            finishing.obsolete = finishingDto.obsolete ?: false

                            logger.info("Successfully updated finishing")

                            finishing
                        }

                        // TODO: Remove from finishing groups as well
                    }
        }
    }

    @Throws(InvalidIdException::class)
    fun delete(id: Long): Uni<Response> {
        return Panache.withTransaction {
            finishingRepository
                    .deleteById(id)
                    .onItem()
                    .transform { _ ->
                        logger.info("Successfully deleted finishing")

                        Response.ok().build()
                    }
                    .onFailure()
                    .transform { e ->
                        logger.error("Couldn't delete finishing: $e")

                        InvalidIdException("finishing")
                    }
        }
    }

    fun makeObsolete(id: Long): Uni<Void> {
        return Panache.withTransaction {
            finishingRepository
                    .findById(id)
                    .onItem()
                    .transformToUni { finishing ->
                        if (finishing == null) {
                            logger.error("Finishing with ID $id is not registered")

                            throw InvalidIdException("finishing")
                        }

                        finishing.obsolete = true

                        logger.info("Successfully marked finishing as obsolete")

                        return@transformToUni Uni.createFrom().voidItem()
                    }
                    .onFailure()
                    .invoke { e -> logger.error("Couldn't mark image as obsolete: $e") }
        }
    }

    fun getGroups(): Multi<FinishingGroup> {
        return finishingGroupRepository.streamAll()
    }

    fun findGroups(groups: Set<FinishingGroupDto>): Multi<FinishingGroup> {
        return finishingGroupRepository.stream(
                groups.stream().map { it.id!! }.collect(Collectors.toSet())
        )
    }

    @Throws(AlreadyExistsException::class, InvalidIdException::class, InvalidNameException::class)
    fun createGroup(name: String, finishings: Set<FinishingDto>): Uni<FinishingGroup> {
        logger.info("Creating group $name with finishings $finishings")

        if (name.isEmpty()) {
            logger.error("Finishing Group name is empty")

            throw InvalidNameException()
        }

        val group = FinishingGroup()
        group.name = name

        val finishingGroupUni = finishingGroupRepository.exists(name)
        val finishingsUni =
                if (finishings.isNotEmpty()) {
                    find(finishings)
                            .filter { finishing -> !finishing.obsolete }
                            .collect()
                            .with(Collectors.toSet())
                } else {
                    Uni.createFrom().item(setOf())
                }

        return Panache.withTransaction {
            Uni.combine()
                    .all()
                    .unis(finishingGroupUni, finishingsUni)
                    .asTuple()
                    .onItem()
                    .transformToUni { tuple ->
                        val exists = tuple.item1
                        val finishingsFetched = tuple.item2

                        if (exists) {
                            logger.error("A finishing group with name $name is already registered")

                            throw AlreadyExistsException("FinishingGroup")
                        } else if (finishingsFetched.size < finishings.size) {
                            logger.error(
                                    "Invalid or repeated finishings detected. Fetched: ${finishingsFetched}"
                            )

                            throw InvalidIdException("finishing")
                        }

                        group.finishings = finishingsFetched

                        finishingGroupRepository
                                .persist(group)
                                .onItem()
                                .invoke { _ -> logger.info("Successfully created finishing group") }
                                .onFailure()
                                .invoke { e ->
                                    logger.error("Couldn't register finishing group: $e")
                                }
                    }
        }
    }

    fun editGroup(group: FinishingGroupDto): Uni<FinishingGroup> {
        logger.info("Updating finishing group $group")

        if (group.id == null || group.id!! <= 0) {
            logger.error("Finishing Group ID is invalid")

            throw InvalidIdException("finishing group")
        } else if (group.name.isNullOrEmpty()) {
            logger.error("Finishing Group name is null or empty")

            throw InvalidNameException()
        } else if (group.finishings == null) {
            group.finishings = setOf()
        }

        val existsUni = finishingGroupRepository.exists(group.name!!, group.id!!)
        val finishingsUni =
                if (group.finishings!!.isNotEmpty()) {
                    find(group.finishings!!)
                            .filter { finishing -> !finishing.obsolete }
                            .collect()
                            .with(Collectors.toSet())
                } else {
                    Uni.createFrom().item(setOf())
                }
        val finishingGroupUni = finishingGroupRepository.findById(group.id!!)

        return Panache.withTransaction {
            Uni.combine()
                    .all()
                    .unis(existsUni, finishingsUni, finishingGroupUni)
                    .asTuple()
                    .onItem()
                    .transform { tuple ->
                        val exists = tuple.item1
                        val finishings = tuple.item2
                        val finishingGroup = tuple.item3

                        if (exists) {
                            logger.error(
                                    "A finishing group with name ${group.name!!} is already registered"
                            )

                            throw AlreadyExistsException("FinishingGroup")
                        } else if (finishingGroup == null) {
                            logger.error("Finishing group with ID ${group.id} is not registered")

                            throw InvalidIdException("finishing group")
                        } else if (finishings.size < group.finishings!!.size) {
                            logger.error(
                                    "Invalid or repeated finishings detected. Fetched: ${finishings}"
                            )

                            throw InvalidIdException("finishing")
                        }

                        finishingGroup.name = group.name!!
                        finishingGroup.finishings = finishings

                        logger.info("Successfully updated finishing group")

                        finishingGroup
                    }
        }
    }

    fun deleteGroup(id: Long): Uni<Response> {
        if (id <= 0) {
            logger.error("Finishing Group ID is invalid")

            throw InvalidIdException("finishing group")
        }

        return Panache.withTransaction {
            finishingGroupRepository
                    .deleteById(id)
                    .onItem()
                    .transform { _ ->
                        logger.info("Successfully deleted finishing group")

                        Response.ok().build()
                    }
                    .onFailure()
                    .transform { e ->
                        logger.error("Couldn't delete finishing group: $e")

                        InvalidIdException("finishing group")
                    }
        }
    }
}
