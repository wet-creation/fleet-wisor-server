package ua.com.fleet_wisor.db.car

import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.*
import ua.com.fleet_wisor.models.car.Maintenance


object MaintenanceTable : Table<Nothing>("maintenance") {
    val id = int("id").primaryKey()
    var carId = int("carId")
    var description = varchar("description")
    var price = double("price")
    var check = varchar("checkUrl")
    var time = datetime("time")
}


fun QueryRowSet.toMaintenance(): Maintenance {
    val t = this
    return Maintenance(
        id = t[MaintenanceTable.id]!!,
        time = t[MaintenanceTable.time].toString(),
        description = t[MaintenanceTable.description]!!,
        checkUrl = t[MaintenanceTable.check]!!,
        car = toCar(),
        price = t[MaintenanceTable.price]!!,
    )
}








