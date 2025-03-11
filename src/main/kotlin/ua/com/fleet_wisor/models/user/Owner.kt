package ua.com.fleet_wisor.models.user

import kotlinx.serialization.Serializable

@Serializable
data class Owner(
    val id: Int = 0,
    val email: String = "",
    val name: String = "",
    val surname: String = "",
    val password: String = "",
)

@Serializable
data class OwnerCreate(
    val email: String,
    val password: String,
    val name: String,
    val surname: String,
)

