package ua.com.fleet_wisor.models.car

import kotlinx.serialization.Serializable


@Serializable
data class CarFillUp(
    val id: Int,
    val timestamp: Long,
    val latitude: Double,
    val longitude: Double,
    val price: Double,
    val car: Car
)

@Serializable
data class CarFillUpCreate(
    val latitude: Double,
    val longitude: Double,
    val price: Double,
    val carId: Int
)
