package ua.com.fleet_wisor.db.reports

import org.apache.commons.io.output.ByteArrayOutputStream
import org.apache.poi.ss.usermodel.Row
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

    override fun mainReportExcel(reports: List<CarPeriodReport>): ByteArray {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Report")

        val header: Row = sheet.createRow(0)
        header.createCell(0).setCellValue("ID")
        header.createCell(1).setCellValue("Колів")
        header.createCell(2).setCellValue("Назва Бренду")
        header.createCell(3).setCellValue("Модель")
        header.createCell(4).setCellValue("К-сть заправок")
        header.createCell(5).setCellValue("Вартість заправок")
        header.createCell(6).setCellValue("К-сть обслуговувань")
        header.createCell(7).setCellValue("Вартість обслуговувань")

        reports.forEachIndexed { index, driver ->
            val row = sheet.createRow(index + 1)
            row.createCell(0).setCellValue(driver.id.toDouble())
            row.createCell(1).setCellValue(driver.color)
            row.createCell(2).setCellValue(driver.brandName)
            row.createCell(3).setCellValue(driver.model)
            row.createCell(4).setCellValue(driver.fillUpCount.toDouble())
            row.createCell(5).setCellValue(driver.totalFillUp)
            row.createCell(6).setCellValue(driver.maintenanceCount.toDouble())
            row.createCell(7).setCellValue(driver.totalMaintenance)
        }

        val outputStream = ByteArrayOutputStream()
        workbook.write(outputStream)
        workbook.close()

        return outputStream.toByteArray()
    }
}
