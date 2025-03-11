package ua.com.fleet_wisor.models.car

import kotlinx.serialization.Serializable

@Serializable
data class Insurance(
    val id: Int,
    val startDate: String,
    val endDate: String,
    val car: Car,
    val photoUrl: String,
)

@Serializable
data class InsuranceCreate(
    val startDate: String,
    val endDate: String,
    val carId: Int,
    val photoUrl: String,
)
