package ua.com.fleet_wisor.db.car

import ua.com.fleet_wisor.db.suspendTransaction
import ua.com.fleet_wisor.db.user.owner.OwnerDao
import ua.com.fleet_wisor.models.car.Car
import ua.com.fleet_wisor.models.car.CarCreate
import ua.com.fleet_wisor.models.user.driver.CarRepository

class CarRepositoryImpl : CarRepository {
    override suspend fun all(): List<Car> = suspendTransaction {
        CarDao.all().map {
            try {
                val car = it.toModel()
                car
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override suspend fun findById(id: Int): Car? = suspendTransaction {
        CarDao.find { CarTable.id eq id }.firstOrNull()?.toModel()

    }

    override suspend fun update(car: Car): Car? {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: Int): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun create(car: CarCreate): Unit = suspendTransaction {
        val lCarBody =
            CarBodyDao.findById(car.carBodyId) ?: throw IllegalStateException("Car body does not exist")
        val lFuelType =
            FuelTypeDao.findById(car.fuelTypeId) ?: throw IllegalStateException("Fuel type does not exist")
        val lOwner = OwnerDao.findById(car.ownerId) ?: throw IllegalStateException("Owner does not exist")

        CarDao.new {
            name = car.name
            brandName = car.brandName
            color = car.color
            vin = car.vin
            model = car.model
            licensePlate = car.licensePlate
            mileAge = car.mileAge
            owner = lOwner
            carBody = lCarBody
            fuelType = lFuelType
        }



    }
}