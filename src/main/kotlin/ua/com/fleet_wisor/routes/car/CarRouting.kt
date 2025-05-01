package ua.com.fleet_wisor.routes.car

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import io.ktor.utils.io.streams.*
import kotlinx.serialization.json.Json
import ua.com.fleet_wisor.minio.MinioService
import ua.com.fleet_wisor.models.car.Car
import ua.com.fleet_wisor.models.car.CarRepository
import ua.com.fleet_wisor.models.user.Owner
import ua.com.fleet_wisor.routes.car.dto.CarCreateApi
import ua.com.fleet_wisor.routes.car.dto.CarFillUpCreate
import ua.com.fleet_wisor.routes.car.dto.InsuranceCreate
import ua.com.fleet_wisor.routes.car.dto.MaintenanceCreate
import ua.com.fleet_wisor.routes.driver.dtos.DriverWithCarCreate
import ua.com.fleet_wisor.utils.notFoundMessage

fun Route.configureCarRouting(
    carRepository: CarRepository
) {
    authenticate {
        route("/cars") {
            post {
                val ownerId =
                    call.principal<UserIdPrincipal>()?.name?.toIntOrNull() ?: throw IllegalArgumentException("Invalid")
                val multiPart = call.receiveMultipart()
                var photoUrl = ""
                multiPart.forEachPart { part ->
                    when (part.name) {
                        "photo" -> {
                            part as PartData.FileItem
                            val contentType = part.contentType?.toString() ?: "application/octet-stream"
                            photoUrl = MinioService.uploadPhotoToMinio(
                                inputStream = part.provider().readBuffer().inputStream(),
                                contentType = contentType,
                            )

                        }

                        "body" -> {
                            part as PartData.FormItem
                            val jsonBody = part.value
                            val carCreateApi = Json.decodeFromString<CarCreateApi>(jsonBody)
                            carRepository.create(
                                ownerId,
                                carCreateApi.carCreate,
                                carCreateApi.insuranceCreate?.copy(photoUrl = photoUrl)
                            )
                        }

                        else -> {
                            call.respond(HttpStatusCode.BadRequest)
                        }
                    }
                    part.dispose()
                }
                call.respond(HttpStatusCode.Created)
            }

            get("/fuel-type") {
                val list = carRepository.allFuelType().map { it.asFuelTypeDto() }
                call.respond(HttpStatusCode.OK, list.sortedBy { it.id })
            }
            get("/car-body") {
                val list = carRepository.allCarBody().map { it.asCarBodyDto() }
                call.respond(HttpStatusCode.OK, list.sortedBy { it.id })
            }



            get("/{id}") {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
                val car = carRepository.findById(id)
                if (car != null) {
                    call.respond(HttpStatusCode.OK, car.asCarDto())
                } else {
                    throw NotFoundException(notFoundMessage(Car::class, id, "Check your id"))
                }
            }
            get {
                val ownerId =
                    call.principal<UserIdPrincipal>()?.name?.toIntOrNull() ?: throw IllegalArgumentException("Invalid")

                val cars = carRepository.all(ownerId).map { it.asCarDto() }
                call.respond(HttpStatusCode.OK, cars)
            }

            put {

            }
            route("/fill-up") {
                get {
                    val carFillUp = carRepository.allFillUps()

                    call.respond(HttpStatusCode.OK, carFillUp.map { it.asCarFillUpDto() })
                }

                post {
                    val carFillUp = call.receive<CarFillUpCreate>()//todo photo
                    carRepository.fillUpCar(carFillUp)
                    call.respond(HttpStatusCode.Created)
                }

            }
            route("/maintenance") {
                get {
                    val maintenance = carRepository.allMaintenance().map { it.asMaintenanceDto() }
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
                    val insurance = carRepository.allInsurances().map { it.asInsuranceDto() }
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
}
