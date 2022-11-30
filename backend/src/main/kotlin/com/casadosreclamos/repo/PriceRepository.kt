package com.casadosreclamos.repo

import com.casadosreclamos.model.request.Measurements
import com.casadosreclamos.model.request.Price
import com.casadosreclamos.model.request.PriceId
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class PriceRepository : PanacheRepositoryBase<Price, PriceId> {

}