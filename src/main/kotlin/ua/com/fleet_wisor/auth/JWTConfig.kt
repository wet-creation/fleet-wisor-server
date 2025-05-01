package ua.com.fleet_wisor.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import ua.com.fleet_wisor.utils.getConfig
import java.time.Instant

object JWTConfig {
    private val secret = getConfig("JWT_SECRET")
    private val issuer = getConfig("ISSUER")
    private val audience = getConfig("AUDIENCE")
    private const val AccessTokenValidity = 30L * 60
    private const val RefreshTokenValidity = 7 * 24 * 60 * 60 * 1000L

    val refreshTokens = mutableMapOf<String, String>()

    private val algorithm = Algorithm.HMAC256(secret)

    fun generateAccessToken(userId: String): String {
        return JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("userId", userId)
            .withClaim("type", "access_token")
            .withExpiresAt(Instant.now().plusSeconds(AccessTokenValidity))
            .sign(algorithm)
    }

    fun generateRefreshToken(userId: String): String {
        return JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("userId", userId)
            .withClaim("type", "refresh_token")
            .withExpiresAt(Instant.now().plusMillis(RefreshTokenValidity))
            .sign(algorithm)
    }

    fun getUserIdFromToken(token: String, isAccess: Boolean): String? {
        val decodedJWT = if (isAccess) verifyAccessToken().verify(token) else verifyRefreshToken().verify(token)
        return decodedJWT.getClaim("userId").asString()
    }

    fun verifyAccessToken(): JWTVerifier = JWT.require(algorithm)
        .withIssuer(issuer)
        .withAudience(audience)
        .withClaim("type", "access_token")
        .build()

    private fun verifyRefreshToken(): JWTVerifier = JWT.require(algorithm)
        .withIssuer(issuer)
        .withAudience(audience)
        .withClaim("type", "refresh_token")
        .build()


}