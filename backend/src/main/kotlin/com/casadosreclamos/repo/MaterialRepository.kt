package com.casadosreclamos.repo

import com.casadosreclamos.model.request.Material
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class MaterialRepository: PanacheRepository<Material>