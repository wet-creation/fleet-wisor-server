package ua.com.fleet_wisor.models.user

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val email: String,
    val name: String,
    val surname: String,
    val password: String,
    val role: Role,
) {
    fun asOwner() = Owner(
        id = id,
        email = email,
        name = name,
        surname = surname
    )
}

@Serializable
data class UserCreate(
    val email: String,
    val password: String,
    val name: String,
    val surname: String,
    val role: Role,
)

@Serializable
data class OwnerCreate(
    val email: String,
    val password: String,
    val name: String,
    val surname: String,
) {
    fun toUser() = UserCreate(
        email = email,
        password = password,
        name = name,
        surname = surname,
        role = Role.OWNER,
    )
}

@Serializable
data class Owner(
    val id: Int,
    val email: String,
    val name: String,
    val surname: String,
)

enum class Role {
    OWNER, DRIVER
}