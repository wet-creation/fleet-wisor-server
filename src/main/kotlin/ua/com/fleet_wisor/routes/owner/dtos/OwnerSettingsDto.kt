package ua.com.fleet_wisor.routes.owner.dtos

import kotlinx.serialization.Serializable
import ua.com.fleet_wisor.routes.car.dto.FuelUnitsDto

@Serializable
data class OwnerSettingsDto(
    val fuelUnits: List<FuelUnitsDto>,
)

@Serializable
data class OwnerSettingsUpsert(
    val fuelUnits: List<OwnerFuelUnitsCreate>,
)

@Serializable
data class OwnerFuelUnitsCreate(
    val idUnit: Int,
    val idFuelType: Int,
)
