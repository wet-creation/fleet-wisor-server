package ua.com.fleet_wisor.routes

import io.ktor.http.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ua.com.fleet_wisor.models.trip.Trip
import ua.com.fleet_wisor.models.trip.TripCreate
import ua.com.fleet_wisor.models.trip.TripRepository
import ua.com.fleet_wisor.utils.notFoundMessage

fun Route.configureTripRouting(
    tripRepository: TripRepository
) {
    route("/trips") {
        post {
            val trip = call.receive<TripCreate>()
            tripRepository.create(trip)
            call.respond(HttpStatusCode.Created)
        }

        get {
            call.respond(HttpStatusCode.OK, tripRepository.all())
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            call.respond(
                HttpStatusCode.OK,
                tripRepository.findById(id) ?: throw NotFoundException(
                    notFoundMessage(
                        Trip::class,
                        id,
                        "Check your id"
                    )
                )
            )

        }


    }


}