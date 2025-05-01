package ua.com.fleet_wisor.routes.car.dto

import kotlinx.serialization.Serializable

@Serializable
data class InsuranceDto(
    val id: Int = -1,
    val startDate: String ="",
    val endDate: String = "",
    val photoUrl: String = "",
    val carId: Int = -1,

)

@Serializable
data class InsuranceCreate(
    val startDate: String,
    val endDate: String,
    val carId: Int = -1,
    val photoUrl: String? = null,
)
