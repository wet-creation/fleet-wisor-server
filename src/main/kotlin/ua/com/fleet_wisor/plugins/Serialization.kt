package ua.com.fleet_wisor.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureContentNegotiation() {
    install(ContentNegotiation) {
        json()
    }
}
