package com.casadosreclamos.model

import io.quarkus.security.jpa.RolesValue

enum class Role(@RolesValue val label: String) {
        COMMERCIAL("Commercial"), // Regular user
        UPLOADER("Uploader"),     // Can upload images
        ADMIN("Admin"),           // Can add new users
}