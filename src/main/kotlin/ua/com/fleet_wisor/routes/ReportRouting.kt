package ua.com.fleet_wisor.routes

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ua.com.fleet_wisor.models.reports.ReportsRepository
import java.time.LocalDate
import java.time.LocalDateTime

fun Route.configureReportRouting(
    reportsRepository: ReportsRepository,
) {
        route("/reports") {
            authenticate {
            get {
                val id = call.principal<UserIdPrincipal>()?.name ?: throw IllegalArgumentException("Invalid")
                println(id)
                val userId = id.toIntOrNull() ?: throw IllegalArgumentException("Invalid userId")
                val start = call.queryParameters["startDate"] ?: throw IllegalArgumentException("Invalid startDate")
                val end = call.queryParameters["endDate"] ?: throw IllegalArgumentException("Invalid endDate")

                val res = reportsRepository.mainReport(
                    LocalDate.parse(start).atStartOfDay(),
                    LocalDate.parse(end).atStartOfDay(),
                    userId
                )
                println(res)

                call.respond(HttpStatusCode.OK, res)

            }
        }

    }
}