package com.casadosreclamos.model

import io.quarkus.security.jpa.Password
import io.quarkus.security.jpa.Roles
import io.quarkus.security.jpa.UserDefinition
import io.quarkus.security.jpa.Username
import javax.persistence.*


@Entity
@UserDefinition
open class User() {
    constructor(name: String, email: String, password: String, roles: MutableSet<Role>) : this() {
        this.name = name
        this.email = email
        this.password = password
        this.roles = roles
    }

    @Id
    @Username
    open lateinit var email: String

    @Password
    open lateinit var password: String

    open lateinit var name: String

    @Roles
    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Role::class, fetch = FetchType.EAGER)
    open lateinit var roles: MutableSet<Role>

    open var credits: Double = 0.0

    // TODO: Add list of brands
}