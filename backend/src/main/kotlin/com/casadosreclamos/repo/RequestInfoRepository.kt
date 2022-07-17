package com.casadosreclamos.repo

import com.casadosreclamos.model.request.RequestInfo
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class RequestInfoRepository: PanacheRepository<RequestInfo>