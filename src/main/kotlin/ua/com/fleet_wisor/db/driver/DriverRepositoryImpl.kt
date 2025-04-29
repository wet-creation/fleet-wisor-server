package ua.com.fleet_wisor.db.driver

import org.ktorm.dsl.*
import ua.com.fleet_wisor.db.car.CarBodyTable
import ua.com.fleet_wisor.db.car.CarFuelTypesTable
import ua.com.fleet_wisor.db.car.CarTable
import ua.com.fleet_wisor.db.car.FuelTypeTable
import ua.com.fleet_wisor.db.mapCollection
import ua.com.fleet_wisor.db.transactionalQuery
import ua.com.fleet_wisor.db.useConnection
import ua.com.fleet_wisor.db.user.OwnerTable
import ua.com.fleet_wisor.models.car.Car
import ua.com.fleet_wisor.models.driver.*
import ua.com.fleet_wisor.routes.driver.dtos.DriverWithCarCreate
import java.time.LocalDate
import java.time.LocalDateTime

class DriverRepositoryImpl : DriverRepository {

    fun mergeDriverWithCars(existing: DriverWithCars, newDriverWithCars: DriverWithCars): DriverWithCars {
        val mergedCars = (existing.cars + newDriverWithCars.cars)
            .groupBy { it.car.id }
            .map { (_, cars) -> cars.reduce(::mergeDriverCar) }

        return existing.copy(cars = mergedCars)
    }

    fun mergeDriverCar(existing: DriverCar, newDriverCar: DriverCar): DriverCar {
        return existing.copy(
            car = mergeCars(existing.car, newDriverCar.car)
        )
    }

    private fun mergeCars(existing: Car, newCar: Car): Car {
        return existing.copy(
            fuelTypes = (existing.fuelTypes + newCar.fuelTypes).distinctBy { it.id }
        )
    }


    override suspend fun findByPhone(phone: String): Driver? {
        return useConnection { database ->
            database.from(DriverTable).innerJoin(OwnerTable, OwnerTable.id eq DriverTable.ownerId).select()
                .where { DriverTable.phone eq phone }.map {
                    it.toDriver()
                }.firstOrNull()
        }

    }

    override suspend fun all(ownerId: Int): List<Driver> {
        return useConnection { database ->
            database.from(DriverTable).innerJoin(OwnerTable, OwnerTable.id eq DriverTable.ownerId)
                .select()
                .where { DriverTable.ownerId eq ownerId }
                .map { it.toDriver() }
        }

    }

    override suspend fun driverWithCars(): List<DriverWithCars> {
        return useConnection { database ->
            database.from(DriverTable)
                .innerJoin(OwnerTable, OwnerTable.id eq DriverTable.ownerId)
                .innerJoin(DriverWithCarTable, DriverWithCarTable.driverId eq DriverTable.id)
                .innerJoin(CarTable, DriverWithCarTable.carId eq CarTable.id)
                .innerJoin(CarBodyTable, CarTable.carBodyId eq CarBodyTable.id)
                .innerJoin(
                    CarFuelTypesTable, CarFuelTypesTable.carId eq CarTable.id
                )
                .innerJoin(
                    FuelTypeTable, FuelTypeTable.id eq CarFuelTypesTable.fuelTypeId
                )
                .select()
                .mapCollection(DriverTable.id, ::mergeDriverWithCars) { it.toDriverWithCars() }
        }

    }

    override suspend fun findById(id: Int): Driver? {
        return useConnection { database ->
            database.from(DriverTable).innerJoin(OwnerTable, OwnerTable.id eq DriverTable.ownerId).select()
                .where { DriverTable.id eq id }
                .map { it.toDriver() }.firstOrNull()
        }

    }

    override suspend fun update(driver: Driver): Driver? {
        return transactionalQuery { database ->
            database.update(DriverTable) {
                set(it.name, driver.name)
                set(it.surname, driver.surname)
                set(it.phone, driver.phone)
                set(it.salary, driver.salary)
                set(it.backLicensePhotoUrl, driver.backLicensePhotoUrl)
                set(it.frontLicensePhotoUrl, driver.frontLicensePhotoUrl)
                set(it.birthday, LocalDate.parse(driver.birthdayDate))
                where { DriverTable.id eq driver.id }
            }
            findById(driver.id)
        }
    }

    override suspend fun delete(id: Int): Boolean {
        return transactionalQuery { database ->
            database.delete(DriverTable) {
                DriverTable.id eq id
            } > 0
        }
    }

    override suspend fun create(driver: DriverCreate) {
        transactionalQuery { database ->
            database.insert(DriverTable) {
                set(DriverTable.ownerId, driver.ownerId)
                set(DriverTable.phone, driver.phone)
                set(DriverTable.driverLicenseNumber, driver.driverLicenseNumber)
                set(DriverTable.salary, driver.salary)
                set(DriverTable.birthday, LocalDate.parse(driver.birthdayDate))
                set(DriverTable.name, driver.name)
                set(DriverTable.surname, driver.surname)
                set(DriverTable.frontLicensePhotoUrl, driver.frontLicensePhotoUrl)
                set(DriverTable.backLicensePhotoUrl, driver.backLicensePhotoUrl)
            }
        }
    }

    override suspend fun assignCar(driverWithCarCreate: DriverWithCarCreate) {
        assignCarToDriver(driverWithCarCreate)
    }
}

suspend fun assignCarToDriver(driverWithCarCreate: DriverWithCarCreate) {
    transactionalQuery { database ->
        database.insert(DriverWithCarTable) {
            set(DriverWithCarTable.carId, driverWithCarCreate.carId)
            set(DriverWithCarTable.driverId, driverWithCarCreate.driverId)
            set(DriverWithCarTable.timestampStart, LocalDateTime.now())
        }
    }
}