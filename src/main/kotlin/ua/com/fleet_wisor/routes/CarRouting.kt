package ua.com.fleet_wisor.routes

import io.ktor.http.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ua.com.fleet_wisor.models.car.CarCreate
import ua.com.fleet_wisor.models.user.driver.CarRepository
import ua.com.fleet_wisor.models.user.owner.Owner
import ua.com.fleet_wisor.utils.notFoundMessage

fun Route.configureCarRouting(
    carRepository: CarRepository
) {
    route("/cars") {
        post {
            val car = call.receive<CarCreate>()
            println(car)
            carRepository.create(car)
            call.respond(HttpStatusCode.Created)
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val car = carRepository.findById(id)
            if (car != null) {
                call.respond(HttpStatusCode.OK, car)
            } else {
                throw NotFoundException(notFoundMessage(Owner::class, id, "Check your id"))
            }
        }
        get {
            val cars = carRepository.all()
            call.respond(HttpStatusCode.OK, cars)
        }

        put {

        }

        delete("/{id}") {

        }
    }
}
