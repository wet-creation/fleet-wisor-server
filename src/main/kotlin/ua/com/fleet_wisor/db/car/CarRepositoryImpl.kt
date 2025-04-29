package ua.com.fleet_wisor.db.car

import org.ktorm.database.Database
import org.ktorm.dsl.*
import ua.com.fleet_wisor.db.driver.DriverTable
import ua.com.fleet_wisor.db.driver.DriverWithCarTable
import ua.com.fleet_wisor.db.transactionalQuery
import ua.com.fleet_wisor.db.user.OwnerTable
import ua.com.fleet_wisor.db.driver.assignCarToDriver
import ua.com.fleet_wisor.db.mapCollection
import ua.com.fleet_wisor.db.useConnection
import ua.com.fleet_wisor.models.car.*
import ua.com.fleet_wisor.routes.driver.dtos.DriverWithCarCreate
import ua.com.fleet_wisor.routes.car.dto.CarCreate
import ua.com.fleet_wisor.routes.car.dto.CarFillUpCreate
import ua.com.fleet_wisor.routes.car.dto.InsuranceCreate
import ua.com.fleet_wisor.routes.car.dto.MaintenanceCreate
import java.time.LocalDate
import java.time.LocalDateTime


class CarRepositoryImpl : CarRepository {

    private fun mergeCars(existing: Car, newCar: Car): Car {
        return existing.copy(
            fuelTypes = existing.fuelTypes + newCar.fuelTypes,
            drivers = existing.drivers + newCar.drivers
        )
    }

    private fun mergeMaintenance(old: Maintenance, new: Maintenance): Maintenance {
        return old.copy(car = mergeCars(old.car, new.car))
    }

    private fun mergeFillUp(existing: CarFillUp, new: CarFillUp): CarFillUp {
        return existing.copy(car = mergeCars(existing.car, new.car))
    }

    private fun mergeInsurance(existing: Insurance, new: Insurance): Insurance {
        return existing.copy(car = mergeCars(existing.car, new.car))
    }

    override suspend fun all(ownerId: Int): List<Car> {
        return useConnection { database ->
            database.buildAllCars()
                .where { CarTable.ownerId eq ownerId }
                .groupBy(
                    CarTable.id,
                    CarFuelTypesTable.carId,
                    CarBodyTable.id,
                    OwnerTable.id,
                    DriverWithCarTable.id,
                    DriverWithCarTable.carId,
                    CarFuelTypesTable.fuelTypeId,
                    FuelTypeTable.id
                ).mapCollection(pkColumn = CarTable.id, merge = ::mergeCars) {
                    it.toCar()
                }


        }
    }

    private fun Database.buildAllCars(): Query {
        return this.from(CarTable)
            .innerJoin(CarBodyTable, CarTable.carBodyId eq CarBodyTable.id)
            .leftJoin(
                CarFuelTypesTable, CarFuelTypesTable.carId eq CarTable.id
            )
            .leftJoin(
                FuelTypeTable, FuelTypeTable.id eq CarFuelTypesTable.fuelTypeId
            )
            .innerJoin(
                OwnerTable, OwnerTable.id eq CarTable.ownerId
            )
            .leftJoin(DriverWithCarTable, CarTable.id eq DriverWithCarTable.carId)
            .leftJoin(
                DriverTable, DriverTable.id eq DriverWithCarTable.driverId
            ).select()
    }

    override suspend fun findById(id: Int): Car? {
        return useConnection { database ->
            database.buildAllCars()
                .where { CarTable.id eq id }
                .mapCollection(CarTable.id, ::mergeCars) {
                    it.toCar()
                }.firstOrNull()
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
            val id = database.insertAndGenerateKey(CarTable) {
                set(it.brandName, car.brandName)
                set(it.color, car.color)
                set(it.vin, car.vin)
                set(it.model, car.model)
                set(it.licensePlate, car.licensePlate)
                set(it.mileAge, car.mileAge)
                set(it.carBodyId, car.carBodyId)
                set(it.ownerId, car.ownerId)
            } as Int

            database.batchInsert(CarFuelTypesTable) {
                car.fuelTypes.forEach { typeId ->
                    item {
                        set(it.carId, id)
                        set(it.fuelTypeId, typeId)
                    }
                }

            }

        }


    }

    override suspend fun fillUpCar(carFillUpCreate: CarFillUpCreate) {
        transactionalQuery { database ->
            database.insert(CarFillUpTable) {
                set(CarFillUpTable.carId, carFillUpCreate.carId)
                set(CarFillUpTable.time, LocalDateTime.parse(carFillUpCreate.time))
                set(CarFillUpTable.checkUrl, carFillUpCreate.checkUrl)
                set(CarFillUpTable.price, carFillUpCreate.price)
                set(CarFillUpTable.unitId, carFillUpCreate.unitId)
            }
        }
    }

    override suspend fun allFillUps(): List<CarFillUp> {
        return useConnection { database ->
            database.from(CarFillUpTable)
                .innerJoin(CarTable, CarFillUpTable.carId eq CarTable.id)
                .innerJoin(CarBodyTable, CarTable.carBodyId eq CarBodyTable.id)
                .innerJoin(
                    CarFuelTypesTable, CarFuelTypesTable.carId eq CarTable.id
                )
                .innerJoin(
                    FuelTypeTable, FuelTypeTable.id eq CarFuelTypesTable.fuelTypeId
                ).innerJoin(
                    OwnerTable, OwnerTable.id eq CarTable.ownerId
                ).select().mapCollection(CarFillUpTable.id, ::mergeFillUp) {
                    it.toFillUp()
                }
        }
    }

    override suspend fun assignDriver(driverWithCarCreate: DriverWithCarCreate) {
        assignCarToDriver(driverWithCarCreate)

    }

    override suspend fun addInsurance(insurance: InsuranceCreate) {
        transactionalQuery { database ->
            database.insert(InsuranceTable) {
                set(it.carId, insurance.carId)
                set(it.endDate, LocalDate.parse(insurance.endDate))
                set(it.startDate, LocalDate.parse(insurance.startDate))
                set(it.photoUrl, insurance.photoUrl)
            }
        }
    }

    override suspend fun allInsurances(): List<Insurance> {
        return useConnection { database ->
            database.from(InsuranceTable)
                .innerJoin(CarTable, InsuranceTable.carId eq CarTable.id)
                .innerJoin(CarBodyTable, CarTable.carBodyId eq CarBodyTable.id)
                .innerJoin(
                    CarFuelTypesTable, CarFuelTypesTable.carId eq CarTable.id
                )
                .innerJoin(
                    FuelTypeTable, FuelTypeTable.id eq CarFuelTypesTable.fuelTypeId
                ).innerJoin(
                    OwnerTable, OwnerTable.id eq CarTable.ownerId
                ).select().mapCollection(CarTable.id, ::mergeInsurance) { it.toInsurance() }
        }
    }

    override suspend fun addMaintenance(maintenance: MaintenanceCreate) {
        transactionalQuery { database ->
            database.insert(MaintenanceTable) {
                set(it.carId, maintenance.carId)
                set(it.price, maintenance.price)
                set(it.check, maintenance.checkUrl)
                set(it.description, maintenance.description)
                set(it.time, LocalDateTime.parse(maintenance.time))
            }
        }
    }

    override suspend fun allMaintenance(): List<Maintenance> {
        return useConnection { database ->
            database.from(MaintenanceTable)
                .innerJoin(CarTable, MaintenanceTable.carId eq CarTable.id)
                .innerJoin(CarBodyTable, CarTable.carBodyId eq CarBodyTable.id)
                .innerJoin(
                    CarFuelTypesTable, CarFuelTypesTable.carId eq CarTable.id
                )
                .innerJoin(
                    FuelTypeTable, FuelTypeTable.id eq CarFuelTypesTable.fuelTypeId
                ).innerJoin(
                    OwnerTable, OwnerTable.id eq CarTable.ownerId
                ).select().mapCollection(MaintenanceTable.id, ::mergeMaintenance) { it.toMaintenance() }

        }
    }

    override suspend fun allCarBody(): List<CarBody> {
        return useConnection { database ->
            database.from(CarBodyTable).select().map { it.toCarBody() }
        }
    }

    override suspend fun allFuelType(): List<FuelType> {
        return useConnection { database ->
            database.from(FuelTypeTable).select().map { it.toFuelType() }
        }
    }
}