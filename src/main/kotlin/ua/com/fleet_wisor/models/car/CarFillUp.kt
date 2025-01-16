package ua.com.fleet_wisor.models.car

import kotlinx.serialization.Serializable
import ua.com.fleet_wisor.models.Position


@Serializable
data class CarFillUp(
    val id: Int,
    val timestamp: Long,
    val position: Position,
    val price: Double,
    val car: Car
)

@Serializable
data class CarFillUpCreate(
    val position: Position,
    val price: Double,
    val carId: Int
)
