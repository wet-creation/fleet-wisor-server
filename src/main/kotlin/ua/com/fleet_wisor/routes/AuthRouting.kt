package ua.com.fleet_wisor.routes

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ua.com.fleet_wisor.auth.JWTConfig
import ua.com.fleet_wisor.models.user.*

fun Route.configureAuthRouting(
    ownerRepository: OwnerRepository,
) {
    route("/auth") {
        post("/login") {
            val request = call.receive<LoginRequest>()
            val user = ownerRepository.findByEmail(request.email)
            if (user == null || !verifyPassword(request.password, user.password)) {
                call.respond(HttpStatusCode.Unauthorized, "Invalid email or password")
                return@post
            }
            val userID = user.id.toString()
            val jwtAccess = JWTConfig.generateAccessToken(userID)
            val jwtRefresh = JWTConfig.generateRefreshToken(userID)

            JWTConfig.refreshTokens[userID] = jwtRefresh

            call.respond(
                HttpStatusCode.OK, JwtTokenResponse(
                    jwtAccessToken = jwtAccess,
                    jwtRefreshToken = jwtRefresh
                )
            )

        }

        post("/refresh") {
            val request = call.receive<JwtRefreshRequest>()
            if (!JWTConfig.refreshTokens.containsValue(request.jwtRefreshToken)) {
                call.respond(HttpStatusCode.Unauthorized, "Invalid refresh token")
                return@post
            }

            val id = JWTConfig.getUserIdFromToken(request.jwtRefreshToken)
                ?: throw IllegalArgumentException("invalid refresh token")
            val jwtRefresh = JWTConfig.generateRefreshToken(id)
            val jwtAccess = JWTConfig.generateAccessToken(id)
            JWTConfig.refreshTokens[id] = jwtRefresh
            call.respond(HttpStatusCode.OK, JwtTokenResponse(jwtAccess, jwtRefresh))


        }
    }

}