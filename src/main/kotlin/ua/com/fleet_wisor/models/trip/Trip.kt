package ua.com.fleet_wisor.models.trip

import kotlinx.serialization.Serializable
import ua.com.fleet_wisor.models.Position

@Serializable
data class Trip(
    val id: Int,
    val name: String,
    val description: String,
    val positionStart: Position,
    val positionEnd: Position,
)

@Serializable
data class TripCreate(
    val name: String,
    val description: String,
    val positionStart: Position,
    val positionEnd: Position,
)