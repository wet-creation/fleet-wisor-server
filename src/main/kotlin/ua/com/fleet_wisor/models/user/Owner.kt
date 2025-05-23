package ua.com.fleet_wisor.models.user

import kotlinx.serialization.Serializable
import ua.com.fleet_wisor.models.car.FuelUnits
import ua.com.fleet_wisor.routes.owner.dtos.OwnerSettingsDto

@Serializable
data class Owner(
    val id: Int = 0,
    val email: String = "",
    val name: String = "",
    val surname: String = "",
    val password: String = "",
) {
    fun asOwnerNoPassword() = OwnerNoPassword(
        id = id,
        email = email,
        name = name,
        surname = surname
    )
}

data class OwnerSettings(
    val fuelUnits: List<FuelUnits>,
) {
    fun asUserSettingsDto(lang: String): OwnerSettingsDto {
        return OwnerSettingsDto(
            fuelUnits = fuelUnits.map { it.asFuelUnits(lang) }

        )
    }
}

@Serializable
data class OwnerNoPassword(
    val id: Int = 0,
    val email: String = "",
    val name: String = "",
    val surname: String = "",
)

@Serializable
data class PasswordUpdate(
    val oldPassword: String = "",
    val newPassword: String = "",
)


@Serializable
data class OwnerCreate(
    val email: String,
    val password: String,
    val name: String,
    val surname: String,
)

