package ua.com.fleet_wisor.routes

import io.ktor.http.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ua.com.fleet_wisor.models.user.driver.Driver
import ua.com.fleet_wisor.models.user.driver.DriverCreate
import ua.com.fleet_wisor.models.user.driver.DriverRepository
import ua.com.fleet_wisor.utils.notFoundMessage

fun Route.configureDriverRouting(
    driverRepository: DriverRepository
) {
    route("/drivers") {
        post {
            val user = call.receive<DriverCreate>()
            driverRepository.create(user)
            call.respond(HttpStatusCode.Created)
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val user = driverRepository.findById(id)
            if (user != null) {
                call.respond(HttpStatusCode.OK, user)
            } else {
                throw NotFoundException(notFoundMessage(Driver::class, id, "Check your id"))
            }
        }
        get {
            val user = driverRepository.all()
            call.respond(HttpStatusCode.OK, user)
        }

        put {

        }

        delete("/{id}") {

        }
    }
}
