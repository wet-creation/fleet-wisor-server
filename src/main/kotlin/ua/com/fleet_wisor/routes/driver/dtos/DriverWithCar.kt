package ua.com.fleet_wisor.routes.driver.dtos

import kotlinx.serialization.Serializable
import ua.com.fleet_wisor.models.driver.Driver
import ua.com.fleet_wisor.routes.car.dto.CarDto

@Serializable
data class DriverWithCarsDto(
    val cars: List<DriverCarDto>,
    val driver: Driver,
)

@Serializable
data class DriverCarDto(
    val timestampStart: String,
    val timestampEnd: String?,
    val car: CarDto,
)


@Serializable
data class DriverWithCarCreate(
    val carId: Int,
    val driverId: Int
)