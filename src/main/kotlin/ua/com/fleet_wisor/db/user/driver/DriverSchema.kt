package ua.com.fleet_wisor.db.user.driver

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import ua.com.fleet_wisor.db.user.UserDao
import ua.com.fleet_wisor.db.user.UserTable
import ua.com.fleet_wisor.models.user.driver.Driver


object DriverTable : IntIdTable("driver") {
    val userId = reference(
        "userId",
        UserTable.id,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    ).uniqueIndex()
    val name = varchar("name", 255)
    val surname = varchar("surname", 255)

    val phone = varchar("phone", 255)
    val driverLicenseNumber = varchar("driverLicenseNumber", 255)
    val uniqueCode = integer("uniqueCode")

}

class DriverDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DriverDao>(DriverTable)

    var user by UserDao referencedOn DriverTable.userId
    var name by DriverTable.name
    var surname by DriverTable.surname
    var phone by DriverTable.phone
    var driverLicenseNumber by DriverTable.driverLicenseNumber
    var uniqueCode by DriverTable.uniqueCode
}


fun DriverDao.toModel() = Driver(
    id = user.id.value,
    email = user.email,
    name = name,
    surname = surname, phone = phone,
    driverLicenseNumber = driverLicenseNumber,
    uniqueCode = uniqueCode
)
