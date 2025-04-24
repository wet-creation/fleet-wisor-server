package ua.com.fleet_wisor.routes

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
import ua.com.fleet_wisor.models.driver.*
import ua.com.fleet_wisor.utils.notFoundMessage

fun Route.configureDriverRouting(
    driverRepository: DriverRepository
) {
    authenticate {
        route("/drivers") {
            post {
                val ownerId =
                    call.principal<UserIdPrincipal>()?.name?.toIntOrNull() ?: throw IllegalArgumentException("Invalid")
                val multiPart = call.receiveMultipart()
                var frontPhotoName = ""
                var backPhotoName = ""
                multiPart.forEachPart { part ->
                    when (part.name) {
                        "frontPhoto" -> {
                            part as PartData.FileItem
                            val contentType = part.contentType?.toString() ?: "application/octet-stream"
                            frontPhotoName = MinioService.uploadPhotoToMinio(
                                inputStream = part.provider().readBuffer().inputStream(),
                                contentType = contentType,
                            )

                        }

                        "backPhoto" -> {
                            part as PartData.FileItem
                            val contentType = part.contentType?.toString() ?: "application/octet-stream"
                            backPhotoName = MinioService.uploadPhotoToMinio(
                                inputStream = part.provider().readBuffer().inputStream(),
                                contentType = contentType,
                            )

                        }

                        "body" -> {

                            part as PartData.FormItem
                            val jsonBody = part.value
                            val driverApi = Json.decodeFromString<DriverCreateApi>(jsonBody)
                            val driver = DriverCreate(
                                name = driverApi.name,
                                ownerId = ownerId,
                                surname = driverApi.surname,
                                phone = driverApi.phone,
                                driverLicenseNumber = driverApi.driverLicenseNumber,
                                frontLicensePhotoUrl = frontPhotoName,
                                backLicensePhotoUrl = backPhotoName,
                                birthdayDate = driverApi.birthdayDate,
                                salary = driverApi.salary
                            )
                            driverRepository.create(driver)

                        }

                        else -> {
                            call.respond(HttpStatusCode.BadRequest)
                        }
                    }
                    part.dispose()
                }


                call.respond(HttpStatusCode.Created)
            }

            route("/{id}") {
                get {
                    val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
                    val user = driverRepository.findById(id)
                    if (user != null) {
                        call.respond(HttpStatusCode.OK, user)
                    } else {
                        throw NotFoundException(notFoundMessage(Driver::class, id, "Check your id"))
                    }
                }
                get("/cars") {
                    val driverWithCars = driverRepository.driverWithCars()
                    call.respond(driverWithCars)
                }
            }
            get {
                val id =
                    call.principal<UserIdPrincipal>()?.name?.toIntOrNull() ?: throw IllegalArgumentException("Invalid")
                val user = driverRepository.all(id)
                call.respond(HttpStatusCode.OK, user)
            }

            put {

            }

            delete("/{id}") {

            }
        }
    }
}
