package ua.com.fleet_wisor.models.car

import ua.com.fleet_wisor.models.user.driver.DriverWithCarCreate

interface CarRepository {

    suspend fun all(): List<Car>
    suspend fun findById(id: Int): Car?
    suspend fun update(car: Car): Car?
    suspend fun delete(id: Int): Boolean
    suspend fun create(car: CarCreate)

    suspend fun fillUpCar(carFillUpCreate: CarFillUpCreate)

    suspend fun allFillUps(): List<CarFillUp>

    suspend fun assignDriver(driverWithCarCreate: DriverWithCarCreate)


}