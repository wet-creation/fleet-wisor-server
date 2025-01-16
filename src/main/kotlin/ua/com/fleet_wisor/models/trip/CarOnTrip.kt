package ua.com.fleet_wisor.models.trip

import kotlinx.serialization.Serializable
import ua.com.fleet_wisor.models.Position
import ua.com.fleet_wisor.models.car.Car


@Serializable
data class CarOnTrip(
    val car: Car,
    val trip: Trip,
    val timestampStart: Double,
    val timestampEnd: Double? = null,
    val currentPosition: Position
)