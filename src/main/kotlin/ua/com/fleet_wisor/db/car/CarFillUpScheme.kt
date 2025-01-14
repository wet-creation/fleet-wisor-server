package ua.com.fleet_wisor.db.car

import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.Table
import org.ktorm.schema.double
import org.ktorm.schema.int
import org.ktorm.schema.long
import ua.com.fleet_wisor.models.car.CarFillUp

object CarFillUpTable: Table<Nothing>("car_fill_up") {

    val id = int("id").primaryKey()
    var timestamp = long("timestamp")
    var latitude = double("latitude")
    var longitude = double("longitude")
    var price = double("price")
    var carId = int("carId")

}

fun QueryRowSet.toFillUp(): CarFillUp {
    val t = this
    return CarFillUp(
        id = t[CarFillUpTable.id]!!,
        timestamp = t[CarFillUpTable.timestamp]!!,
        latitude = t[CarFillUpTable.latitude]!!,
        longitude = t[CarFillUpTable.longitude]!!,
        price = t[CarFillUpTable.price]!!,
        car = toCar()
    )
}