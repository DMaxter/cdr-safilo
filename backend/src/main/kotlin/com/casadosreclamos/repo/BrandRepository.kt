package com.casadosreclamos.repo

import com.casadosreclamos.model.request.Brand
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class BrandRepository: PanacheRepository<Brand> {
}