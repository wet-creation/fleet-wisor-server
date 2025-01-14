package ua.com.fleet_wisor.models.user.driver

import kotlinx.serialization.Serializable
import ua.com.fleet_wisor.models.car.Car

@Serializable
data class DriverWithCar(
    val id: Int,
    val timestampStart: Long,
    val timestampEnd: Long?,
    val car: Car,
    val driver: Driver,
)


@Serializable
data class DriverWithCarCreate(
    val carId: Int,
    val driverId: Int
)