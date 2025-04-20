package ua.com.fleet_wisor.routes

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ua.com.fleet_wisor.models.reports.CarPeriodReport
import ua.com.fleet_wisor.models.reports.ReportsRepository
import java.time.LocalDate

fun Route.configureReportRouting(
    reportsRepository: ReportsRepository,
) {
    route("/reports") {
        authenticate {
            get {
                val res = getReports(reportsRepository)
                println(res)

                call.respond(HttpStatusCode.OK, res)
            }

            get("/excel") {
                val res = getReports(reportsRepository)
                val excel = reportsRepository.mainReportExcel(res)

                call.response.header(
                    HttpHeaders.ContentDisposition,
                    ContentDisposition.Attachment.withParameter(ContentDisposition.Parameters.FileName, "report.xlsx")
                        .toString()
                )

                call.respondBytes(
                    bytes = excel,
                    contentType = ContentType.Application.Xlsx
                )
            }
        }


    }
}

private fun RoutingContext.getReports(reportsRepository: ReportsRepository): List<CarPeriodReport> {
    val id = call.principal<UserIdPrincipal>()?.name ?: throw IllegalArgumentException("Invalid")
    println(id)
    val userId = id.toIntOrNull() ?: throw IllegalArgumentException("Invalid userId")
    val start = call.queryParameters["startDate"] ?: throw IllegalArgumentException("Invalid startDate")
    val end = call.queryParameters["endDate"] ?: throw IllegalArgumentException("Invalid endDate")

    return reportsRepository.mainReport(
        LocalDate.parse(start).atStartOfDay(),
        LocalDate.parse(end).atStartOfDay(),
        userId
    )
}