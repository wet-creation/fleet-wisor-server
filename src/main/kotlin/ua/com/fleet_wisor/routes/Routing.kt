package ua.com.fleet_wisor.routes

import io.ktor.server.application.*
import io.ktor.server.routing.*
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

    }

}