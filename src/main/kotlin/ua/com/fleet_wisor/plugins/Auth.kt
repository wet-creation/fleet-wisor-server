package ua.com.fleet_wisor.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import ua.com.fleet_wisor.auth.JWTConfig

fun Application.configureAuth() {
    install(Authentication) {
        jwt {
            realm = "Ktor Server"
            verifier(JWTConfig.verifyToken())
            validate { credentials ->
                if (credentials.payload.getClaim("userId").asString() != "") {
                    UserIdPrincipal(credentials.payload.getClaim("userId").asString())
                } else {
                    null
                }
            }

            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
}