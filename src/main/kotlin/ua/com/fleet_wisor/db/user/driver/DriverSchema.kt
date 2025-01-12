package ua.com.fleet_wisor.db.user.driver

import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import ua.com.fleet_wisor.db.user.UserTable
import ua.com.fleet_wisor.models.user.driver.Driver


object DriverTable : Table<Nothing>("driver") {
    val userId = int("userId").primaryKey()
    var phone = varchar("phone")
    var driverLicenseNumber = varchar("driverLicenseNumber")
    var uniqueCode = int("uniqueCode")

}

fun QueryRowSet.toDriver(): Driver {
    val t = this
    return Driver(
        email = t[UserTable.email]!!,
        id = t[UserTable.id]!!,
        name = t[UserTable.name]!!,
        surname = t[UserTable.surname]!!,
        phone = t[DriverTable.phone]!!,
        driverLicenseNumber = t[DriverTable.driverLicenseNumber]!!,
        uniqueCode = t[DriverTable.uniqueCode]!!,
    )
}



