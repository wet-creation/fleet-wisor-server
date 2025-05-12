package ua.com.fleet_wisor.models.car

import ua.com.fleet_wisor.routes.car.dto.FuelUnitsDto

data class FuelUnits(
    val id: Int,
    val nameUk: String,
    val nameEn: String,
    val fuelType: FuelType = FuelType(),
) {
    fun asFuelUnits(lang: String): FuelUnitsDto {
        val isEn = lang.equals("en", ignoreCase = true)
        return FuelUnitsDto(
            id = id,
            name = if (isEn) nameEn else nameUk,
        )
    }
}
