package ua.com.fleet_wisor.db.car

import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import ua.com.fleet_wisor.db.user.toUser
import ua.com.fleet_wisor.models.car.Car
import ua.com.fleet_wisor.models.car.CarBody
import ua.com.fleet_wisor.models.car.FuelType
import ua.com.fleet_wisor.models.user.User


object CarBodyTable : Table<Nothing>("car_body") {
    val id = int("id").primaryKey()
    var name = varchar("name")
}

fun QueryRowSet.toCarBody(): CarBody {
    val t = this
    return CarBody(
        id = t[CarTable.id]!!,
        name = t[CarTable.name]!!,
    )
}


object FuelTypeTable : Table<Nothing>("fuel_type") {
    val id = int("id").primaryKey()
    var name = varchar("name")
}

fun QueryRowSet.toFuelType(): FuelType {
    val t = this
    return FuelType(
        id = t[FuelTypeTable.id]!!,
        name = t[FuelTypeTable.name]!!,
    )
}


object CarTable : Table<Nothing>("car") {
    val id = int("id").primaryKey()
    var name = varchar("name")
    var brandName = varchar("brandName")
    var color = varchar("color")
    var vin = varchar("vin")
    var model = varchar("model")
    var licensePlate = varchar("licensePlate")
    var mileAge = int("mileAge")
    var ownerId = int("ownerId")
    var carBodyId = int("carBodyId")
    var fuelTypeId = int("fuelTypeId")
}

fun QueryRowSet.toCar(): Car {
    val t = this
    return Car(
        id = t[CarTable.id]!!,
        name = t[CarTable.name]!!,
        brandName = t[CarTable.brandName]!!,
        color = t[CarTable.color],
        vin = t[CarTable.vin],
        model = t[CarTable.model],
        licensePlate = t[CarTable.licensePlate],
        mileAge = t[CarTable.mileAge]!!,
        owner = toUser(),
        fuelType = toFuelType(),
        carBody = toCarBody(),
    )
}






