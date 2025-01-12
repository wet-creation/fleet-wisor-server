package ua.com.fleet_wisor.db.user.driver

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.timestamp

object DriverWithCarTable : Table<Nothing>("driver_with_car") {
    val id = int("id").primaryKey()
    var timestampStart = timestamp("timestampStart")
    var timestampEnd = timestamp("timestampEnd")
    var driverId = int("driverId")
    var carId = int("carId")
}


