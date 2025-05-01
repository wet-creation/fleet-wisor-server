package ua.com.fleet_wisor.routes.car.dto

import kotlinx.serialization.Serializable
import ua.com.fleet_wisor.models.driver.Driver
import ua.com.fleet_wisor.models.user.Owner


@Serializable
data class CarBodyDto(val id: Int = -1, val name: String = "")

@Serializable
data class SimpleFuelTypeDto(val id: Int = -1, val name: String = "")

@Serializable
data class FuelTypeDto(val id: Int = -1, val name: String = "", val units: List<FuelUnitsDto> = emptyList())

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
    val fuelTypes: List<SimpleFuelTypeDto> = listOf(),
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
    val drivers: List<Int>,
    val ownerId: Int = -1,
    val fuelTypes: List<Int>,
    val carBodyId: Int,
)
@Serializable
data class CarCreateApi(
    val carCreate: CarCreate,
    val insuranceCreate: InsuranceCreate? = null,
)

