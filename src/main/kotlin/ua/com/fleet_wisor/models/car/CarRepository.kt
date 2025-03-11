package ua.com.fleet_wisor.models.car

import ua.com.fleet_wisor.models.driver.DriverWithCarCreate

interface CarRepository {

    suspend fun all(): List<Car>
    suspend fun findById(id: Int): Car?
    suspend fun update(car: Car): Car?
    suspend fun delete(id: Int): Boolean
    suspend fun create(car: CarCreate)

    suspend fun fillUpCar(carFillUpCreate: CarFillUpCreate)

    suspend fun allFillUps(): List<CarFillUp>

    suspend fun assignDriver(driverWithCarCreate: DriverWithCarCreate)

    suspend fun addInsurance(insurance: InsuranceCreate)
    suspend fun allInsurances(): List<Insurance>

    suspend fun addMaintenance(maintenance: MaintenanceCreate)
    suspend fun allMaintenance(): List<Maintenance>
    suspend fun allCarBody(): List<CarBody>
    suspend fun allFuelType(): List<FuelType>
}
