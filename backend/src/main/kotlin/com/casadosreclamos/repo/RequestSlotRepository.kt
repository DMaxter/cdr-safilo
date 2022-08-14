package com.casadosreclamos.repo

import com.casadosreclamos.model.request.RequestSlot
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class RequestSlotRepository: PanacheRepository<RequestSlot>