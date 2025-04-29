package ua.com.fleet_wisor.db.user

import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import ua.com.fleet_wisor.db.car.toFuelType
import ua.com.fleet_wisor.models.car.FuelUnits
import ua.com.fleet_wisor.models.user.Owner


object OwnerTable : Table<Nothing>("owner") {
    val id = int("id").primaryKey()
    var email = varchar("email")
    var name = varchar("name")
    var surname = varchar("surname")
    var password = varchar("password")
}

object OwnerUnitTable : Table<Nothing>("owner_fuel_units") {
    val ownerId = int("ownerId").primaryKey()
    var fuelTypeId = int("fuelTypeId").primaryKey()
    var unitId = int("unitId")
}

object FuelUnitsTable : Table<Nothing>("fuel_units") {
    val id = int("id").primaryKey()
    val fuelTypeId = int("fuelTypeId")
    var nameUk = varchar("nameUk")
    var nameEn = varchar("nameEn")
}

fun QueryRowSet.toFuelUnits(): FuelUnits {
    val t = this
    return FuelUnits(
        id = t[FuelUnitsTable.id]!!,
        nameUk = t[FuelUnitsTable.nameUk]!!,
        nameEn = t[FuelUnitsTable.nameEn]!!,
        fuelType = toFuelType()
    )
}



fun QueryRowSet.toUser(): Owner {
    val t = this
    return Owner(
        email = t[OwnerTable.email]!!,
        id = t[OwnerTable.id]!!,
        password = t[OwnerTable.password]!!,
        name = t[OwnerTable.name]!!,
        surname = t[OwnerTable.surname]!!,
    )
}


