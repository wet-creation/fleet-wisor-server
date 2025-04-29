package ua.com.fleet_wisor.models.car

import ua.com.fleet_wisor.routes.car.dto.FuelUnitsDto

data class FuelUnits(
    val id: Int,
    val nameUk: String,
    val nameEn: String,
    val fuelType: FuelType,
) {
    fun asFuelUnits(): FuelUnitsDto {
        return FuelUnitsDto(
            id = id,
            name = nameUk,
            fuelType = fuelType.asFuelTypeDto()
        )
    }
}
