package ua.com.fleet_wisor.db.reports

import org.apache.commons.io.output.ByteArrayOutputStream
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import ua.com.fleet_wisor.db.DatabaseFactory.database
import ua.com.fleet_wisor.models.reports.CarPeriodReport
import ua.com.fleet_wisor.models.reports.ReportsRepository
import java.sql.Timestamp
import java.time.LocalDateTime

class ReportsRepositoryImpl : ReportsRepository {

    override fun mainReport(
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        ownerId: Int
    ): List<CarPeriodReport> {
        val list = mutableListOf<CarPeriodReport>()
        database.useConnection {
            val expression =
                """                  
                    SELECT 
                        car.id,
                        car.color,
                        car.brandName,
                        car.model,
                        COALESCE(f.fillup_count, 0) AS fillup_count,
                        COALESCE(f.total_fillup, 0) AS total_fillup,
                        COALESCE(m.maintenance_count, 0) AS maintenance_count,
                        COALESCE(m.total_maintenance, 0) AS total_maintenance
                    FROM car
                  
                    LEFT JOIN (
                        SELECT carid, COUNT(*) AS fillup_count, SUM(price) AS total_fillup
                        FROM car_fill_up
                        WHERE time BETWEEN ? AND ?
                        GROUP BY carid
                    ) f ON car.id = f.carid
                    LEFT JOIN (
                        SELECT carid, COUNT(*) AS maintenance_count, SUM(price) AS total_maintenance
                        FROM maintenance
                        WHERE time BETWEEN ? AND ?
                        GROUP BY carid
                    ) m ON car.id = m.carid
                    WHERE car.ownerId = ?;
                """

            it.prepareStatement(expression).use { statement ->
                val start = Timestamp.valueOf(startDateTime)
                val end = Timestamp.valueOf(endDateTime)

                statement.setTimestamp(1, start)
                statement.setTimestamp(2, end)
                statement.setTimestamp(3, start)
                statement.setTimestamp(4, end)
                statement.setInt(5, ownerId)
                val rs = statement.executeQuery()
                while (rs.next()) {
                    val carStats = CarPeriodReport(
                        id = rs.getInt("id"),
                        color = rs.getString("color"),
                        brandName = rs.getString("brandName"),
                        model = rs.getString("model"),
                        fillUpCount = rs.getInt("fillup_count"),
                        totalFillUp = rs.getDouble("total_fillup"),
                        maintenanceCount = rs.getInt("maintenance_count"),
                        totalMaintenance = rs.getDouble("total_maintenance")
                    )
                    list.add(carStats)
                }
            }
        }
        return list
    }

    override fun mainReportExcel(
        lang: String,
        reports: List<CarPeriodReport>,
        startDate: String,
        endDate: String
    ): ByteArray {
        val workbook = XSSFWorkbook()
        val localization = if (lang == "en") EnglishReportLocalizations else UkraineReportLocalizations
        val sheet = workbook.createSheet(localization.fileName(startDate, endDate))
        val headerFont = workbook.createFont().apply {
            bold = true
            color = IndexedColors.WHITE.index
        }

        val headerStyle = workbook.createCellStyle().apply {
            setFont(headerFont)
            fillForegroundColor = IndexedColors.TEAL.index
            fillPattern = FillPatternType.SOLID_FOREGROUND
            alignment = HorizontalAlignment.CENTER
            verticalAlignment = VerticalAlignment.CENTER
            borderBottom = BorderStyle.THIN
            borderTop = BorderStyle.THIN
            borderLeft = BorderStyle.THIN
            borderRight = BorderStyle.THIN
        }
        val bodyStyle = workbook.createCellStyle().apply {
            borderBottom = BorderStyle.THIN
            borderTop = BorderStyle.THIN
            borderLeft = BorderStyle.THIN
            borderRight = BorderStyle.THIN
        }

        val header = sheet.createRow(0)
        val headers = listOf(
            (localization.id),
            (localization.color),
            (localization.brandName),
            (localization.model),
            (localization.fillUpCount),
            (localization.totalFillUp),
            (localization.maintenanceCount),
            (localization.totalMaintenance),
        )
        headers.forEachIndexed { index, headerName ->
            val cell = header.createCell(index)
            cell.setCellValue(headerName)
            cell.cellStyle = headerStyle
        }
        reports.forEachIndexed { index, driver ->
            val row = sheet.createRow(index + 1)
            row.addCell(0, driver.id, bodyStyle)
            row.addCell(1, driver.color ?: "", bodyStyle)
            row.addCell(2, driver.brandName, bodyStyle)
            row.addCell(3, driver.model ?: "", bodyStyle)
            row.addCell(4, driver.fillUpCount, bodyStyle)
            row.addCell(5, driver.totalFillUp, bodyStyle)
            row.addCell(6, driver.maintenanceCount, bodyStyle)
            row.addCell(7, driver.totalMaintenance, bodyStyle)
        }

        (0..7).forEach { sheet.autoSizeColumn(it) }



        val outputStream = ByteArrayOutputStream()
        workbook.write(outputStream)
        workbook.close()

        return outputStream.toByteArray()
    }
}

private fun Row.addCell(index: Int, value: Any, style: CellStyle) {
    val cell = createCell(index)
    when (value) {
        is String -> cell.setCellValue(value)
        is Double -> cell.setCellValue(value)
        is Int -> cell.setCellValue(value.toDouble())
        is Float -> cell.setCellValue(value.toDouble())
        else -> cell.setCellValue(value.toString())
    }
    cell.cellStyle = style
}

interface ReportLocalizations {
    fun fileName(from: String, to: String): String
    val fillUpCount: String
    val totalFillUp: String
    val maintenanceCount: String
    val totalMaintenance: String
    val id: String
    val color: String
    val brandName: String
    val model: String
}

object EnglishReportLocalizations : ReportLocalizations {
    override fun fileName(from: String, to: String): String = "$from to $to"
    override val fillUpCount: String = "Fillup count"
    override val totalFillUp: String = "Total fillup"
    override val maintenanceCount: String = "Maintenance count"
    override val totalMaintenance: String = "Total maintenance"
    override val id: String = "ID"
    override val color: String = "Color"
    override val brandName: String = "Brand name"
    override val model: String = "Model"
}

object UkraineReportLocalizations : ReportLocalizations {
    override fun fileName(from: String, to: String): String = "$from по $to"
    override val fillUpCount: String
        get() = "К-сть заправок"
    override val totalFillUp: String
        get() = "Вартість заправок"
    override val maintenanceCount: String
        get() = "К-сть обслуговувань"
    override val totalMaintenance: String
        get() = "Вартість обслуговувань"
    override val id: String
        get() = "ID"
    override val color: String
        get() = "Колір"
    override val brandName: String
        get() = "Назва Бренду"
    override val model: String
        get() = "Модель"

}
