package ua.com.fleet_wisor.db.car

import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.*
import ua.com.fleet_wisor.db.user.toFuelUnits
import ua.com.fleet_wisor.models.car.CarFillUp

object CarFillUpTable : Table<Nothing>("car_fill_up") {

    val id = int("id").primaryKey()
    var time = datetime("time")
    var checkUrl = varchar("checkUrl")
    var price = double("price")
    var amount = double("amount")
    var carId = int("carId")
    var unitId = int("unitId")

}

fun QueryRowSet.toFillUp(): CarFillUp {
    val t = this
    return CarFillUp(
        id = t[CarFillUpTable.id]!!,
        time = t[CarFillUpTable.time].toString(),
        price = t[CarFillUpTable.price]!!,
        amount = t[CarFillUpTable.amount]!!,
        car = toCar(),
        fuelUnits = toFuelUnits(),
        checkUrl = t[CarFillUpTable.checkUrl]!!,
    )
}