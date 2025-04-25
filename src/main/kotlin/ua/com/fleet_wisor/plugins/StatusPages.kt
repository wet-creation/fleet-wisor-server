package ua.com.fleet_wisor.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*


fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause" , status = HttpStatusCode.InternalServerError)
            println(cause.stackTraceToString())
        }
        exception<NotFoundException> { call, cause ->
            call.respondText(text = "404: ${cause.message}" , status = HttpStatusCode.NotFound)
        }
//        exception<IllegalArgumentException> { call, cause ->
//            call.respondText(text = "400: ${cause.message}" , status = HttpStatusCode.BadRequest)
//        }
        exception<BadRequestException> { call, cause ->
            call.respondText(text = "400: ${cause.message}", status = HttpStatusCode.BadRequest)
        }
    }
}