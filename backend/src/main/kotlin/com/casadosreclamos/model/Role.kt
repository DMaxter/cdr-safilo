package com.casadosreclamos.model

import io.quarkus.security.jpa.RolesValue

const val COMMERCIAL_ROLE = "Commercial"
const val UPLOADER_ROLE = "Uploader"
const val CDR_ROLE = "CDR"
const val MANAGER_ROLE = "Manager"
const val ADMIN_ROLE = "Admin"

enum class Role(@RolesValue val label: String) {
        COMMERCIAL(COMMERCIAL_ROLE), // Regular user
        UPLOADER(UPLOADER_ROLE),     // Can upload images
        CDR(CDR_ROLE),               // CDR User
        MANAGER(MANAGER_ROLE),       // Can add new users/clients
        ADMIN(ADMIN_ROLE),           // Can attribute roles to users
}