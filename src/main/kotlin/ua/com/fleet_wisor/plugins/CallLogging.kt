package ua.com.fleet_wisor.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.request.*
import org.slf4j.event.Level

fun Application.configureCallPlugin() {
    install(CallLogging) {
        level = Level.WARN
        filter { call ->
            call.request.path().startsWith("/api/v1")
        }
        format { call ->
            val endpoint = call.request.path().substringAfter("/api/v1")
            val status = call.response.status()
            val headers = call.request.headers["Authorization"]
            val httpMethod = call.request.httpMethod.value
            val userAgent = call.request.headers["User-Agent"]
            """
                
               Authorization: $headers
               Endpoint: $endpoint 
               Status: $status
               HTTP method: $httpMethod
               User agent: $userAgent""""
                .trimMargin()
        }
    }
}