package ua.com.fleet_wisor.routes.car.dto

import kotlinx.serialization.Serializable

@Serializable
data class CarFillUpDto(
    val id: Int,
    val time: String,
    val price: Double,
    val amount: Double,
    val checkUrl: String,
    val fuelType: FuelTypeDto,
    val unit: FuelUnitsDto,
    val car: CarDto
)

@Serializable
data class CarFillUpUpdate(
    val id: Int,
    val time: String,
    val price: Double,
    val amount: Double,
    val checkUrl: String,
)
@Serializable
data class CarFillUpCreate(
    val time: String,
    val price: Double,
    val unitId: Int,
    val carId: Int,
    val amount: Double,
    val checkUrl: String = ""
)
