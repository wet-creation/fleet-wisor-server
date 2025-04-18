package ua.com.fleet_wisor.db.reports

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
}
