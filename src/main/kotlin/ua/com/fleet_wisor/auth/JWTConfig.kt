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
    private const val AccessTokenValidity = 30000L
    private const val RefreshTokenValidity = 7 * 24 * 60 * 60 * 1000L

    val refreshTokens = mutableMapOf<String, String>()

    private val algorithm = Algorithm.HMAC256(secret)

    fun generateAccessToken(userId: String): String {
        return JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("userId", userId)
            .withExpiresAt(Instant.now().plusMillis(AccessTokenValidity))
            .sign(algorithm)
    }

    fun generateRefreshToken(userId: String): String {
        return JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("userId", userId)
            .withExpiresAt(Instant.now().plusMillis(RefreshTokenValidity))
            .sign(algorithm)
    }

    fun getUserIdFromToken(token: String): String? {
        val decodedJWT = verifyToken().verify(token)
        return decodedJWT.getClaim("userId").asString()
    }

    fun verifyToken(): JWTVerifier = JWT.require(algorithm)
        .withIssuer(issuer)
        .withAudience(audience)
        .build()


}