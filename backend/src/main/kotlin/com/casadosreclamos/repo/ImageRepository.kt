package com.casadosreclamos.repo

import com.casadosreclamos.model.request.Image
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import io.quarkus.panache.common.Parameters
import io.smallrye.mutiny.Multi
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ImageRepository: PanacheRepository<Image> {
    fun findAll(images: Set<Long>): Multi<Image> {
        return stream("FROM Images i WHERE i.id in (:images)", Parameters.with("images", images).map())
    }
}