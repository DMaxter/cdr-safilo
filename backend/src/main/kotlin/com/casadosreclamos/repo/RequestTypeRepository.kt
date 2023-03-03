package com.casadosreclamos.repo

import com.casadosreclamos.model.request.RequestType
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class RequestTypeRepository: PanacheRepository<RequestType>