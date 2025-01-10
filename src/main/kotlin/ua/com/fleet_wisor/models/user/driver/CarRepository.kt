package ua.com.fleet_wisor.models.user.driver

import ua.com.fleet_wisor.models.car.Car
import ua.com.fleet_wisor.models.car.CarCreate

interface CarRepository {

    suspend fun all(): List<Car>
    suspend fun findById(id: Int): Car?
    suspend fun update(car: Car): Car?
    suspend fun delete(id: Int): Boolean
    suspend fun create(car: CarCreate)


}