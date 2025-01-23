package ua.com.fleet_wisor.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ua.com.fleet_wisor.minio.MinioService
import ua.com.fleet_wisor.models.user.UserRepository
import ua.com.fleet_wisor.models.car.CarRepository
import ua.com.fleet_wisor.models.trip.TripRepository
import ua.com.fleet_wisor.models.user.driver.DriverRepository

fun Application.configureRouting(
    userRepository: UserRepository,
    driverRepository: DriverRepository,
    carRepository: CarRepository,
    tripRepository: TripRepository,
) {
    routing {
        route("/api/v1") {
            configureOwnerRouting(userRepository)
            configureDriverRouting(driverRepository)
            configureCarRouting(carRepository)
            configureTripRouting(tripRepository)
            configureAuthRouting(userRepository)
        }

        route("/image/{filename}") {
            get {
                val filename =
                    call.parameters["filename"] ?: return@get call.respond(
                        HttpStatusCode.BadRequest,
                        "Missing filename"
                    )
                val stream = MinioService.load(filename)
                val fileType = MinioService.getContentType(filename)
                call.response.header(HttpHeaders.ContentType, fileType.contentType)
                call.respondOutputStream(status = HttpStatusCode.OK, contentType = fileType) {
                    stream.copyTo(this)
                }
            }
        }


    }

}