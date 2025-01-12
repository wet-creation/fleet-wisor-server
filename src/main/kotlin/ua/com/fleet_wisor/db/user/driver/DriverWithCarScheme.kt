package ua.com.fleet_wisor.db.user.driver

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import ua.com.fleet_wisor.db.car.CarDao
import ua.com.fleet_wisor.db.car.CarTable

object DriverWithCarTable : IntIdTable("driver_with_car") {
    val timestampStart = long("timestampStart")
    val timestampEnd = long("timestampEnd").nullable()

    val driverId = integer("driverId").references(DriverTable.id)
    val carId = integer("carId").references(CarTable.id)

}


class DriverWithCarDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DriverWithCarDao>(DriverWithCarTable)

    var timestampStart by DriverWithCarTable.timestampStart
    var timestampEnd by DriverWithCarTable.timestampEnd
    var driverId by DriverDao referencedOn DriverWithCarTable.driverId
    var carId by CarDao referencedOn DriverWithCarTable.carId


}

