package ua.com.fleet_wisor.models.driver

import ua.com.fleet_wisor.models.car.Car

data class DriverWithCars(
    val cars: List<DriverCar>,
    val driver: Driver,
)

data class DriverCar(
    val timestampStart: String,
    val timestampEnd: String?,
    val car: Car,
)

