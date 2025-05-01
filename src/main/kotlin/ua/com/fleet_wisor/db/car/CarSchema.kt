package ua.com.fleet_wisor.db.car

import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.long
import org.ktorm.schema.varchar
import ua.com.fleet_wisor.db.driver.toLeftJoinDriver
import ua.com.fleet_wisor.db.user.toLeftJoinFuelTypeUnits
import ua.com.fleet_wisor.db.user.toUser
import ua.com.fleet_wisor.models.car.Car
import ua.com.fleet_wisor.models.car.CarBody
import ua.com.fleet_wisor.models.car.FuelType


object CarBodyTable : Table<Nothing>("car_body") {
    val id = int("id").primaryKey()
    var nameUk = varchar("nameUk")
    var nameEn = varchar("nameEn")
}

fun QueryRowSet.toCarBody(): CarBody {
    val t = this
    return CarBody(
        id = t[CarBodyTable.id]!!,
        nameUk = t[CarBodyTable.nameUk]!!,
        nameEn = t[CarBodyTable.nameEn]!!,
    )
}


object FuelTypeTable : Table<Nothing>("fuel_type") {
    val id = int("id").primaryKey()
    var nameUk = varchar("nameUk")
    var nameEn = varchar("nameEn")
}

object CarFuelTypesTable : Table<Nothing>("car_fuel_types") {
    val carId = int("carId").primaryKey()
    var fuelTypeId = int("fuelTypeId").primaryKey()
}


fun QueryRowSet.toFuelType(): FuelType {
    val t = this
    val units = toLeftJoinFuelTypeUnits()

    return FuelType(
        id = t[FuelTypeTable.id]!!,
        nameUk = t[FuelTypeTable.nameUk]!!,
        nameEn = t[FuelTypeTable.nameEn]!!,
        fuelUnits = if (units != null) listOf(units) else emptyList(),
    )
}

fun QueryRowSet.toLeftJoinFuelType(): FuelType? {
    val t = this
    if (t[FuelTypeTable.id] == null)
        return null
    return toFuelType()
}


object CarTable : Table<Nothing>("car") {
    val id = int("id").primaryKey()
    var brandName = varchar("brandName")
    var color = varchar("color")
    var vin = varchar("vin")
    var model = varchar("model")
    var licensePlate = varchar("licensePlate")
    var mileAge = long("mileAge")
    var ownerId = int("ownerId")
    var carBodyId = int("carBodyId")
}


fun QueryRowSet.toCar(): Car {
    val row = this
    val carId = row[CarTable.id]!!

    val fuelType = row.toLeftJoinFuelType()
    val driver = row.toLeftJoinDriver()

    val car = Car(
        id = carId,
        brandName = row[CarTable.brandName]!!,
        color = row[CarTable.color],
        vin = row[CarTable.vin],
        model = row[CarTable.model],
        licensePlate = row[CarTable.licensePlate],
        mileAge = row[CarTable.mileAge]!!,
        owner = row.toUser(),
        drivers = if (driver != null) setOf(driver) else emptySet(),
        carBody = row.toCarBody(),
        fuelTypes = if (fuelType != null) setOf(fuelType) else emptySet(),
    )

    return car
}







