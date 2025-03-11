package ua.com.fleet_wisor.routes

import io.ktor.http.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ua.com.fleet_wisor.models.car.*
import ua.com.fleet_wisor.models.user.Owner
import ua.com.fleet_wisor.models.driver.DriverWithCarCreate
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

        get("/fuel-type") {
            val list = carRepository.allFuelType()
            call.respond(HttpStatusCode.OK, list.sortedBy { it.id })
        }
        get("/car-body") {
            val list = carRepository.allCarBody()
            call.respond(HttpStatusCode.OK, list.sortedBy { it.id })
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
                val carFillUp = call.receive<CarFillUpCreate>()//todo photo
                carRepository.fillUpCar(carFillUp)
                call.respond(HttpStatusCode.Created)
            }

        }
        route("/maintenance") {
            get {
                val maintenance = carRepository.allMaintenance()
                call.respond(HttpStatusCode.OK, maintenance)
            }

            post {
                val maintenance = call.receive<MaintenanceCreate>()//todo photo
                carRepository.addMaintenance(maintenance)
                call.respond(HttpStatusCode.Created)
            }

        }
        route("/insurance") {
            get {
                val insurance = carRepository.allInsurances()
                call.respond(HttpStatusCode.OK, insurance)
            }

            post {
                val insurance = call.receive<InsuranceCreate>() //todo photo
                carRepository.addInsurance(insurance)
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
            else throw NotFoundException(notFoundMessage(Owner::class, id, "Check your id"))
        }
    }
}
