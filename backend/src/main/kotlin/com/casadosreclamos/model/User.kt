package com.casadosreclamos.model

import io.quarkus.security.jpa.Password
import io.quarkus.security.jpa.Roles
import io.quarkus.security.jpa.UserDefinition
import io.quarkus.security.jpa.Username
import javax.persistence.*


@Entity
@UserDefinition
class User {
    @Id
    @Username
    lateinit var email: String

    @Password
    lateinit var password: String

    lateinit var name: String

    @Roles
    @Enumerated
    @ElementCollection(targetClass = Role::class, fetch = FetchType.EAGER)
    lateinit var roles: MutableList<Role>

    var credits: Double = 0.0

    // TODO: Add list of brands
}