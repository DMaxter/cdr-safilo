package com.casadosreclamos.repo

import com.casadosreclamos.model.Request
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class RequestRepository: PanacheRepository<Request>