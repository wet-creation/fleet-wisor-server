package ua.com.fleet_wisor.routes

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ua.com.fleet_wisor.models.user.owner.OwnerRepository

fun Application.configureRouting(
    ownerRepository: OwnerRepository
) {
    routing {
        route("/api/v1") {

            configureOwnerRouting(ownerRepository)

        }

    }

}