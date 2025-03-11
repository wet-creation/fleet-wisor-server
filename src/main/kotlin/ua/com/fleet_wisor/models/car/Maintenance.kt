package ua.com.fleet_wisor.models.car

import kotlinx.serialization.Serializable

@Serializable
data class Maintenance(
    val id: Int,
    val time: String,
    val description: String,
    val checkUrl: String,
    val car: Car,
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