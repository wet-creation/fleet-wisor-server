package ua.com.fleet_wisor.models.driver

import kotlinx.serialization.Serializable
import ua.com.fleet_wisor.models.user.Owner

@Serializable
data class Driver(
    val id: Int,
    val owner: Owner,
    val name: String,
    val surname: String,
    val phone: String,
    val driverLicenseNumber: String,
    val frontLicensePhotoUrl: String,
    val backLicensePhotoUrl: String,
    val birthdayDate: String,
    val salary: Double,
)



@Serializable
data class DriverCreate(
    val ownerId: Int,
    val name: String,
    val surname: String,
    val phone: String,
    val driverLicenseNumber: String,
    val frontLicensePhotoUrl: String,
    val backLicensePhotoUrl: String,
    val birthdayDate: String,
    val salary: Double,
)
@Serializable
data class DriverCreateApi(
    val ownerId: Int,
    val name: String,
    val surname: String,
    val phone: String,
    val driverLicenseNumber: String,
    val birthdayDate: String,
    val salary: Double,
)
