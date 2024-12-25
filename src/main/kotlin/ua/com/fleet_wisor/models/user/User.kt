package ua.com.fleet_wisor.models.user

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val email: String,
    val password: String,
    val role: Role,
) {

}
@Serializable
data class UserCreate(
    val email: String,
    val password: String,
    val role: Role,
)



enum class Role {
    OWNER, DRIVER
}