package ua.com.fleet_wisor.routes.car.dto

import kotlinx.serialization.Serializable


@Serializable
data class MaintenanceDto(
    val id: Int,
    val time: String,
    val description: String,
    val checkUrl: String,
    val car: CarDto,
    val price: Double,
)

@Serializable
data class MaintenanceCreate(
    val time: String,
    val description: String,
    val checkUrl: String,
    val carId: Int,
    val price: Double,
)