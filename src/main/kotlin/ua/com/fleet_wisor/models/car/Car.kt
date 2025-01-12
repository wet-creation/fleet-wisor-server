package ua.com.fleet_wisor.models.car

import kotlinx.serialization.Serializable
import ua.com.fleet_wisor.models.user.Owner

@Serializable
data class CarBody(val id: Int, val name: String)

@Serializable
data class FuelType(val id: Int, val name: String)


@Serializable
data class Car(
    val id: Int,
    val name: String,
    val brandName: String,
    val color: String?,
    val vin: String?,
    val model: String?,
    val licensePlate: String?,
    val mileAge: Int,
    val owner: Owner,
    val fuelType: FuelType,
    val carBody: CarBody,
)



@Serializable
data class CarCreate(
    val name: String,
    val brandName: String,
    val color: String?,
    val vin: String?,
    val model: String?,
    val licensePlate: String?,
    val mileAge: Int,
    val ownerId: Int,
    val fuelTypeId: Int,
    val carBodyId: Int,
)
