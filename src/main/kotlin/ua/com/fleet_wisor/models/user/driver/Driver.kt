package ua.com.fleet_wisor.models.user.driver

import kotlinx.serialization.Serializable

@Serializable
data class Driver(
    val id: Int,
    val name: String,
    val surname: String,
    val email: String,
    val phone: String,
    val driverLicenseNumber: String,
    val uniqueCode: Int
)


@Serializable
data class DriverCreate(
    val email: String,
    val password: String,
    val name: String,
    val surname: String,
    val phone: String,
    val driverLicenseNumber: String,
    val uniqueCode: Int
)
