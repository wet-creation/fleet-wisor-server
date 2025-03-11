package ua.com.fleet_wisor.models.car

import kotlinx.serialization.Serializable
import ua.com.fleet_wisor.models.user.Owner

@Serializable
data class CarBody(val id: Int = -1, val name: String = "")

@Serializable
data class FuelType(val id: Int, val name: String)


@Serializable
data class Car(
    val id: Int = -1,
    val brandName: String = "",
    val color: String? = null,
    val vin: String? = null,
    val model: String? = null,
    val licensePlate: String? = null,
    val mileAge: Long = 0,
    val owner: Owner = Owner(),
    val fuelTypes: List<FuelType> = listOf(),
    val carBody: CarBody = CarBody(),
)


@Serializable
data class CarCreate(
    val brandName: String,
    val color: String?,
    val vin: String?,
    val model: String?,
    val licensePlate: String?,
    val mileAge: Long,
    val ownerId: Int,
    val fuelTypes: List<Int>,
    val carBodyId: Int,
)
