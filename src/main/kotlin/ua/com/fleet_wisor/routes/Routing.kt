package ua.com.fleet_wisor.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ua.com.fleet_wisor.minio.MinioService
import ua.com.fleet_wisor.models.user.OwnerRepository
import ua.com.fleet_wisor.models.car.CarRepository
import ua.com.fleet_wisor.models.driver.DriverRepository
import ua.com.fleet_wisor.models.reports.ReportsRepository
import ua.com.fleet_wisor.routes.car.configureCarRouting
import ua.com.fleet_wisor.routes.driver.configureDriverRouting

fun Application.configureRouting(
    ownerRepository: OwnerRepository,
    driverRepository: DriverRepository,
    carRepository: CarRepository,
    reportRepository: ReportsRepository,
) {
    routing {
        route("/") {
            get {
                call.respond(HttpStatusCode.OK, " World")
            }
        }
        route("/api/v1") {
            configureOwnerRouting(ownerRepository)
            configureDriverRouting(driverRepository)
            configureCarRouting(carRepository)
            configureAuthRouting(ownerRepository)
            configureReportRouting(reportRepository)
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