package ua.com.fleet_wisor.models.reports

import java.time.LocalDateTime

interface ReportsRepository {
    fun mainReport(startDateTime: LocalDateTime, endDateTime: LocalDateTime, ownerId: Int): List<CarPeriodReport>
    fun mainReportExcel(
        lang: String,
        reports: List<CarPeriodReport>,
        startDate: String,
        endDate: String
    ): ByteArray
}