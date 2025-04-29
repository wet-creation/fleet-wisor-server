package ua.com.fleet_wisor.routes.car.dto

import kotlinx.serialization.Serializable
import ua.com.fleet_wisor.models.driver.Driver
import ua.com.fleet_wisor.models.user.Owner


@Serializable
data class CarBodyDto(val id: Int = -1, val name: String = "")

@Serializable
data class FuelTypeDto(val id: Int = -1, val name: String = "")

@Serializable
data class CarDto(
    val id: Int = -1,
    val brandName: String = "",
    val color: String? = null,
    val vin: String? = null,
    val model: String? = null,
    val licensePlate: String? = null,
    val mileAge: Long = 0,
    val owner: Owner = Owner(),
    val drivers: List<Driver> = listOf(),
    val fuelTypes: List<FuelTypeDto> = listOf(),
    val carBody: CarBodyDto = CarBodyDto(),
)


@Serializable
data class CarCreate(
    val brandName: String,
    val color: String?,
    val vin: String?,
    val model: String?,
    val licensePlate: String?,
    val mileAge: Long,
    val ownerId: Int,
    val fuelTypes: List<Int>,
    val carBodyId: Int,
)
