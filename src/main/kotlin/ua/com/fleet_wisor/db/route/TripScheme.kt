package ua.com.fleet_wisor.db.trip

import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.*
import ua.com.fleet_wisor.models.Position
import ua.com.fleet_wisor.models.trip.Trip

object TripTable: Table<Nothing>("trip") {
    val id = int("id").primaryKey()
    var name = varchar("name")
    var description = varchar("description")
    var longitudeStart = double("longitudeStart")
    var latitudeStart = double("latitudeStart")
    var latitudeEnd = double("latitudeEnd")
    var longitudeEnd = double("longitudeEnd")
}


object CarOnTripTable: Table<Nothing>("trip") {
    val carId = int("carId").primaryKey()
    val tripId = int("tripId").primaryKey()
    val timestampStart = long("timestampStart")
    val timestampEnd = long("timestampEnd")
    var latitude = double("latitude")
    var longitude = double("longitude")
}





fun QueryRowSet.toTrip(): Trip {
    val t = this
    return Trip(
        id = t[TripTable.id]!!,
        name = t[TripTable.name]!!,
        description = t[TripTable.description]!!,
        positionStart = Position(
            longitude = t[TripTable.longitudeStart]!!,
            latitude = t[TripTable.latitudeStart]!!,
        ),
        positionEnd = Position(
            longitude = t[TripTable.longitudeEnd]!!,
            latitude = t[TripTable.latitudeEnd]!!,
        ),
    )
}
