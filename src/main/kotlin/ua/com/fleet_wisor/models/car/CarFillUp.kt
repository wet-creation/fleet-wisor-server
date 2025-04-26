package ua.com.fleet_wisor.models.car

import kotlinx.serialization.Serializable


@Serializable
data class CarFillUp(
    val id: Int,
    val time: String,
    val price: Double,
    val amount: Double,
    val checkUrl: String,
    val car: Car
)

@Serializable
data class CarFillUpCreate(
    val price: Double,
    val checkUrl: String,
    val time: String,
    val carId: Int
)
