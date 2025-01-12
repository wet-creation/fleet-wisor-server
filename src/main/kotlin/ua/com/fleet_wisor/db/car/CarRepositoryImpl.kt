package ua.com.fleet_wisor.db.car

import org.ktorm.dsl.*
import ua.com.fleet_wisor.db.transactionalQuery
import ua.com.fleet_wisor.db.user.UserTable
import ua.com.fleet_wisor.models.car.Car
import ua.com.fleet_wisor.models.car.CarCreate
import ua.com.fleet_wisor.models.user.driver.CarRepository

class CarRepositoryImpl : CarRepository {
    override suspend fun all(): List<Car> {
        return transactionalQuery { database ->
            database.from(CarTable)
                .innerJoin(CarBodyTable, CarTable.carBodyId eq CarBodyTable.id)
                .innerJoin(
                    FuelTypeTable, FuelTypeTable.id eq CarTable.fuelTypeId
                ).innerJoin(
                    UserTable, UserTable.id eq CarTable.ownerId
                ).select().map { it.toCar() }
        }
    }

    override suspend fun findById(id: Int): Car? {
        return transactionalQuery { database ->
            database.from(CarTable)
                .innerJoin(CarBodyTable, CarTable.carBodyId eq CarBodyTable.id)
                .innerJoin(
                    FuelTypeTable, FuelTypeTable.id eq CarTable.fuelTypeId
                ).innerJoin(
                    UserTable, UserTable.id eq CarTable.ownerId
                ).select().map { it.toCar() }.firstOrNull()
        }
    }

    override suspend fun update(car: Car): Car? {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: Int): Boolean {
        return transactionalQuery { database ->
            database.delete(CarTable) {
                it.id eq id
            } == 1
        }
    }

    override suspend fun create(car: CarCreate) {
        transactionalQuery { database ->
            database.insert(CarTable) {
                set(it.name, car.name)
                set(it.brandName, car.brandName)
                set(it.color, car.color)
                set(it.vin, car.vin)
                set(it.model, car.model)
                set(it.licensePlate, car.licensePlate)
                set(it.mileAge, car.mileAge)
                set(it.carBodyId, car.carBodyId)
                set(it.ownerId, car.ownerId)
                set(it.fuelTypeId, car.fuelTypeId)
            }
        }


    }
}