package ua.com.fleet_wisor.routes.car.dto

import kotlinx.serialization.Serializable

@Serializable
data class CarFillUpDto(
    val id: Int,
    val time: String,
    val price: Double,
    val amount: Double,
    val checkUrl: String,
    val car: CarDto
)
@Serializable
data class CarFillUpCreate(
    val price: Double,
    val checkUrl: String,
    val time: String,
    val carId: Int
)
