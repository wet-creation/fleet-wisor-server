package ua.com.fleet_wisor.models.driver

import kotlinx.serialization.Serializable
import ua.com.fleet_wisor.models.car.Car

@Serializable
data class DriverWithCars(
    val cars: List<DriverCar>,
    val driver: Driver,
)

@Serializable
data class DriverCar(
    val timestampStart: String,
    val timestampEnd: String?,
    val car: Car,
)


@Serializable
data class DriverWithCarCreate(
    val carId: Int,
    val driverId: Int
)