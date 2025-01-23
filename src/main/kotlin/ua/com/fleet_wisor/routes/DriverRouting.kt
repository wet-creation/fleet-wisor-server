package ua.com.fleet_wisor.routes

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import io.ktor.utils.io.streams.*
import kotlinx.serialization.json.Json
import ua.com.fleet_wisor.minio.MinioService
import ua.com.fleet_wisor.models.user.driver.Driver
import ua.com.fleet_wisor.models.user.driver.DriverCreate
import ua.com.fleet_wisor.models.user.driver.DriverRepository
import ua.com.fleet_wisor.models.user.hashPassword
import ua.com.fleet_wisor.utils.notFoundMessage

fun Route.configureDriverRouting(
    driverRepository: DriverRepository
) {
    route("/drivers") {
        post {
            val multiPart = call.receiveMultipart()
            var imageName = ""
            multiPart.forEachPart { part ->
                when (part.name) {
                    "image" -> {
                        println("Hello from file")
                        part as PartData.FileItem
                        val contentType = part.contentType?.toString() ?: "application/octet-stream"
                        imageName = MinioService.uploadPhotoToMinio(
                            inputStream = part.provider().readBuffer().inputStream(),
                            contentType = contentType,
                        )

                    }

                    "body" -> {
                        part as PartData.FormItem
                        val jsonBody = part.value
                        val driver = Json.decodeFromString<DriverCreate>(jsonBody)
                        val hashPassword = hashPassword(driver.password)
                        val userWithHashPassword = DriverCreate(
                            email = driver.email,
                            password = hashPassword,
                            name = driver.name,
                            surname = driver.surname,
                            phone = driver.phone,
                            driverLicenseNumber = driver.driverLicenseNumber,
                            uniqueCode = driver.uniqueCode,
                            imageUrl = imageName,
                        )
                        driverRepository.create(userWithHashPassword)

                    }

                    else -> {
                        call.respond(HttpStatusCode.BadRequest)
                    }
                }
                part.dispose()
            }


            call.respond(HttpStatusCode.Created)
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val user = driverRepository.findById(id)
            if (user != null) {
                call.respond(HttpStatusCode.OK, user)
            } else {
                throw NotFoundException(notFoundMessage(Driver::class, id, "Check your id"))
            }
        }
        get {
            val user = driverRepository.all()
            call.respond(HttpStatusCode.OK, user)
        }

        put {

        }

        delete("/{id}") {

        }
    }
}
