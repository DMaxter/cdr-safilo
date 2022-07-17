package com.casadosreclamos.repo

import com.casadosreclamos.model.request.Image
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ImageRepository: PanacheRepository<Image>