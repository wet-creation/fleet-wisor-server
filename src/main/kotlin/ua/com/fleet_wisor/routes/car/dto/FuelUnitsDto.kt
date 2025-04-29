package ua.com.fleet_wisor.routes.car.dto

import kotlinx.serialization.Serializable

@Serializable
data class FuelUnitsDto(
    val id: Int,
    val name: String,
    val fuelType: FuelTypeDto,
)
