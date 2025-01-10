package ua.com.fleet_wisor.db.car

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import ua.com.fleet_wisor.db.user.owner.OwnerDao
import ua.com.fleet_wisor.db.user.owner.OwnerTable
import ua.com.fleet_wisor.db.user.owner.toModel
import ua.com.fleet_wisor.models.car.Car
import ua.com.fleet_wisor.models.car.CarBody
import ua.com.fleet_wisor.models.car.FuelType


object CarBodyTable : IntIdTable("car_body") {
    val name = varchar("name", 255)
}

class CarBodyDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CarBodyDao>(CarBodyTable)

    var name by CarBodyTable.name
}

fun CarBodyDao.toModel(): CarBody = CarBody(id.value, name)

object FuelTypeTable : IntIdTable("fuel_type") {
    val name = varchar("name", 255)
}

class FuelTypeDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<FuelTypeDao>(FuelTypeTable)

    var name by FuelTypeTable.name
}

fun FuelTypeDao.toModel(): FuelType = FuelType(id.value, name)


object CarTable : IntIdTable("car") {
    val name = varchar("name", 255)
    val brandName = varchar("brandName", 255)
    val color = varchar("color", 50).nullable()
    val vin = varchar("vin", 18).nullable()
    val model = varchar("model", 255).nullable()
    val licensePlate = varchar("licensePlate", 50).nullable()
    val mileAge = integer("mileAge").default(0)
    val ownerId = reference(
        "ownerId",
        OwnerTable.id,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    ).uniqueIndex()
    val carBodyId = reference(
        "carBodyId",
        CarBodyTable.id,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    ).uniqueIndex()
    val fuelTypeId = reference(
        "fuelTypeId",
        FuelTypeTable.id,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    ).uniqueIndex()
}


class CarDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CarDao>(CarTable)

    var name by CarTable.name
    var brandName by CarTable.brandName
    var color by CarTable.color
    var vin by CarTable.vin
    var model by CarTable.model
    var licensePlate by CarTable.licensePlate
    var mileAge by CarTable.mileAge
    var owner by OwnerDao referencedOn CarTable.ownerId
    var carBody by CarBodyDao referencedOn CarTable.carBodyId
    var fuelType by FuelTypeDao referencedOn CarTable.fuelTypeId
}

fun CarDao.toModel(): Car =
    Car(
        id = id.value,
        name = name,
        brandName = brandName,
        color = color,
        vin = vin,
        model = model,
        licensePlate = licensePlate,
        mileAge = mileAge,
        fuelType = fuelType.toModel(),
        carBody = carBody.toModel(),
        owner = owner.toModel()
    )




