package ua.com.fleet_wisor.models.user

import at.favre.lib.crypto.bcrypt.BCrypt
import kotlinx.serialization.Serializable

@Serializable
data class JwtTokenResponse(
    val jwtAccessToken: String,
    val jwtRefreshToken: String,
)

@Serializable
data class JwtRefreshRequest(
    val jwtRefreshToken: String,
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class RegisterRequest(
    val name: String,
    val surname: String,
    val email: String,
    val password: String
)



fun verifyPassword(password: String, hashPassword: String): Boolean {
    return BCrypt.verifyer().verify(password.toCharArray(), hashPassword).verified
}

fun hashPassword(password: String): String {
    return BCrypt.withDefaults().hashToString(12, password.toCharArray())
}

