package ua.com.fleet_wisor.routes

import io.ktor.http.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ua.com.fleet_wisor.models.car.Car
import ua.com.fleet_wisor.models.car.CarCreate
import ua.com.fleet_wisor.models.car.CarFillUpCreate
import ua.com.fleet_wisor.models.car.CarRepository
import ua.com.fleet_wisor.models.user.User
import ua.com.fleet_wisor.models.user.driver.DriverWithCarCreate
import ua.com.fleet_wisor.utils.notFoundMessage

fun Route.configureCarRouting(
    carRepository: CarRepository
) {
    route("/cars") {
        post {
            val car = call.receive<CarCreate>()
            carRepository.create(car)
            call.respond(HttpStatusCode.Created)
        }



        get("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val car = carRepository.findById(id)
            if (car != null) {
                call.respond(HttpStatusCode.OK, car)
            } else {
                throw NotFoundException(notFoundMessage(Car::class, id, "Check your id"))
            }
        }
        get {
            val cars = carRepository.all()
            call.respond(HttpStatusCode.OK, cars)
        }

        put {

        }
        route("/fill-up") {
            get {
                val carFillUp = carRepository.allFillUps()
                call.respond(HttpStatusCode.OK, carFillUp)
            }

            post {
                val carFillUp = call.receive<CarFillUpCreate>()
                carRepository.fillUpCar(carFillUp)
                call.respond(HttpStatusCode.Created)
            }


        }
        route("/driver") {
            post {
                val driverWithCarCreate = call.receive<DriverWithCarCreate>()
                carRepository.assignDriver(driverWithCarCreate)
                call.respond(HttpStatusCode.Created)
            }
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            if (carRepository.delete(id))
                call.respond(HttpStatusCode.OK)
            else throw NotFoundException(notFoundMessage(User::class, id, "Check your id"))
        }
    }
}
