package ua.com.fleet_wisor.routes

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ua.com.fleet_wisor.models.user.OwnerRepository
import ua.com.fleet_wisor.models.car.CarRepository
import ua.com.fleet_wisor.models.user.driver.DriverRepository

fun Application.configureRouting(
    ownerRepository: OwnerRepository,
    driverRepository: DriverRepository,
    carRepository: CarRepository,
) {
    routing {
        route("/api/v1") {

            configureOwnerRouting(ownerRepository)
            configureDriverRouting(driverRepository)
            configureCarRouting(carRepository)
        }

    }

}