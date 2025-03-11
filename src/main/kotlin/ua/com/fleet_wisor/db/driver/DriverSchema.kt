package ua.com.fleet_wisor.db.driver

import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.*
import ua.com.fleet_wisor.db.car.toCar
import ua.com.fleet_wisor.db.user.OwnerTable
import ua.com.fleet_wisor.models.driver.Driver
import ua.com.fleet_wisor.models.driver.DriverCar
import ua.com.fleet_wisor.models.driver.DriverWithCars
import ua.com.fleet_wisor.models.user.Owner


object DriverTable : Table<Nothing>("driver") {
    val id = int("id").primaryKey()
    val ownerId = int("ownerId")
    var phone = varchar("phone")
    var name = varchar("name")
    var surname = varchar("surname")
    var driverLicenseNumber = varchar("driverLicenseNumber")
    var frontLicensePhotoUrl = varchar("frontLicensePhotoUrl")
    var backLicensePhotoUrl = varchar("backLicensePhotoUrl")
    var salary = double("salary")
    var birthday = date("birthday")

}

fun QueryRowSet.toDriver(): Driver {
    val t = this
    return Driver(
        id = t[DriverTable.id]!!,
        name = t[DriverTable.name]!!,
        surname = t[DriverTable.surname]!!,
        phone = t[DriverTable.phone]!!,
        driverLicenseNumber = t[DriverTable.driverLicenseNumber]!!,
        owner = Owner(
            id = t[DriverTable.ownerId]!!,
            email = t[OwnerTable.email]!!,
            name = t[OwnerTable.name]!!,
            surname = t[OwnerTable.surname]!!,
            password = t[OwnerTable.password]!!,
        ),
        frontLicensePhotoUrl = t[DriverTable.frontLicensePhotoUrl]!!,
        backLicensePhotoUrl = t[DriverTable.backLicensePhotoUrl]!!,
        birthdayDate = t[DriverTable.birthday]!!.toString(),
        salary = t[DriverTable.salary]!!,
    )
}

fun QueryRowSet.toDriverWithCars(): DriverWithCars {
    val t = this
    val driver = Driver(
        id = t[DriverTable.id]!!,
        name = t[DriverTable.name]!!,
        surname = t[DriverTable.surname]!!,
        phone = t[DriverTable.phone]!!,
        driverLicenseNumber = t[DriverTable.driverLicenseNumber]!!,
        owner = Owner(
            id = t[DriverTable.ownerId]!!,
            email = t[OwnerTable.email]!!,
            name = t[OwnerTable.name]!!,
            surname = t[OwnerTable.surname]!!,
            password = t[OwnerTable.password]!!,
        ),
        frontLicensePhotoUrl = t[DriverTable.frontLicensePhotoUrl]!!,
        backLicensePhotoUrl = t[DriverTable.backLicensePhotoUrl]!!,
        birthdayDate = t[DriverTable.birthday]!!.toString(),
        salary = t[DriverTable.salary]!!,
    )

    return DriverWithCars(
        cars = listOf(toDriverCar()),
        driver = driver
    )

}


fun QueryRowSet.toDriverCar(): DriverCar {
    val t = this
    return DriverCar(
        timestampStart = t[DriverWithCarTable.timestampStart]?.toString() ?: "",
        timestampEnd = t[DriverWithCarTable.timestampEnd]?.toString(),
        car = toCar()
    )
}



