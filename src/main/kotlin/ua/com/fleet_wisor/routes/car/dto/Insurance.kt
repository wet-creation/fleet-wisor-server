package ua.com.fleet_wisor.routes.car.dto

import kotlinx.serialization.Serializable

@Serializable
data class InsuranceDto(
    val id: Int,
    val startDate: String,
    val endDate: String,
    val car: CarDto,
    val photoUrl: String,
)

@Serializable
data class InsuranceCreate(
    val startDate: String,
    val endDate: String,
    val carId: Int,
    val photoUrl: String,
)
