package ua.com.fleet_wisor.models

import kotlinx.serialization.Serializable

@Serializable
data class Position (
    val latitude: Double,
    val longitude: Double,
)