package ua.com.fleet_wisor.models.user.owner

import kotlinx.serialization.Serializable

@Serializable
data class Owner(
    val id: Int,
    val name: String,
    val surname: String,
    val email: String,
    val password: String
) {
    fun asOwnerRead() = OwnerRead(id = id, name = name, surname = surname, email = email)
}

@Serializable
data class OwnerRead(
    val id: Int,
    val name: String,
    val surname: String,
    val email: String,
)


@Serializable
data class OwnerCreate(
    val name: String,
    val surname: String,
    val email: String,
    val password: String
)
