package ua.com.fleet_wisor.db.driver

import org.ktorm.schema.*

object DriverWithCarTable : Table<Nothing>("driver_with_car") {
    val id = int("id").primaryKey()
    var timestampStart = datetime("timestampStart")
    var timestampEnd = datetime("timestampEnd")
    var driverId = int("driverId")
    var carId = int("carId")
}


