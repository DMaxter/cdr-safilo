package com.casadosreclamos.repo

import com.casadosreclamos.model.Address
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class AddressRepository: PanacheRepository<Address>