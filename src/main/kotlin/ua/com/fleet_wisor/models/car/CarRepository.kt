package ua.com.fleet_wisor.models.car

import ua.com.fleet_wisor.routes.driver.dtos.DriverWithCarCreate
import ua.com.fleet_wisor.routes.car.dto.CarCreate
import ua.com.fleet_wisor.routes.car.dto.CarFillUpCreate
import ua.com.fleet_wisor.routes.car.dto.CarUpdate
import ua.com.fleet_wisor.routes.car.dto.InsuranceCreate
import ua.com.fleet_wisor.routes.car.dto.InsuranceDto
import ua.com.fleet_wisor.routes.car.dto.MaintenanceCreate

interface CarRepository {

    suspend fun all(ownerId: Int): List<Car>
    suspend fun findById(id: Int): Car?
    suspend fun update(car: CarUpdate): Car?
    suspend fun delete(id: Int): Boolean
    suspend fun create(ownerId: Int, car: CarCreate, insurance: InsuranceCreate? = null)

    suspend fun fillUpCar(carFillUpCreate: CarFillUpCreate)

    suspend fun allFillUps(): List<CarFillUp>
    suspend fun findFillUpById(id: Int): CarFillUp?

    suspend fun findInsuranceById(id: Int): Insurance?

    suspend fun assignDriver(driverWithCarCreate: DriverWithCarCreate)
    suspend fun addInsurance(insurance: InsuranceCreate)
    suspend fun allInsurances(): List<Insurance>

    suspend fun getByCarInsurances(carId: Int): Insurance?
    suspend fun addMaintenance(maintenance: MaintenanceCreate)
    suspend fun allMaintenance(): List<Maintenance>
    suspend fun allCarBody(): List<CarBody>
    suspend fun allFuelType(): List<FuelType>
    suspend fun updateInsurance(insuranceUpdate: InsuranceDto): Insurance?
}
