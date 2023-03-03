package com.casadosreclamos.repo

import com.casadosreclamos.model.Banner
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class BannerRepository : PanacheRepositoryBase<Banner, String>