package ua.com.fleet_wisor.models.car

import ua.com.fleet_wisor.models.driver.Driver
import ua.com.fleet_wisor.models.user.Owner
import ua.com.fleet_wisor.routes.car.dto.CarBodyDto
import ua.com.fleet_wisor.routes.car.dto.CarDto
import ua.com.fleet_wisor.routes.car.dto.FuelTypeDto
import ua.com.fleet_wisor.routes.car.dto.SimpleFuelTypeDto

data class CarBody(val id: Int = -1, val nameUk: String = "", val nameEn: String = "") {
    fun asCarBodyDto(lang: String): CarBodyDto {
        val isEn = lang.contains("en", ignoreCase = true)
        return CarBodyDto(
            id = id, name = if (isEn) nameEn else nameUk,
        )
    }
}

data class FuelType(
    val id: Int = -1,
    val nameUk: String = "",
    val nameEn: String = "",
    val fuelUnits: List<FuelUnits> = emptyList()
) {
    fun asSimpleFuelTypeDto(lang: String): SimpleFuelTypeDto {
        val isEn = lang.contains("en", ignoreCase = true)
        return SimpleFuelTypeDto(id, if (isEn) nameEn else nameUk)
    }

    fun asFuelTypeDto(lang: String): FuelTypeDto {
        val isEn = lang.contains("en", ignoreCase = true)
        return FuelTypeDto(id, if (isEn) nameEn else nameUk, units = fuelUnits.map { it.asFuelUnits(lang) })
    }
}


data class Car(
    val id: Int = -1,
    val brandName: String = "",
    val color: String? = null,
    val vin: String? = null,
    val model: String? = null,
    val licensePlate: String? = null,
    val mileAge: Long = 0,
    val owner: Owner = Owner(),
    val drivers: Set<Driver> = setOf(),
    val fuelTypes: Set<FuelType> = setOf(),
    val carBody: CarBody = CarBody(),
) {
    fun asCarDto(lang: String) = CarDto(
        id = id,
        brandName = brandName,
        color = color,
        vin = vin,
        model = model,
        licensePlate = licensePlate,
        mileAge = mileAge,
        owner = owner,
        drivers = drivers.toList(),
        fuelTypes = fuelTypes.map { it.asSimpleFuelTypeDto(lang) },
        carBody = carBody.asCarBodyDto(lang)
    )
}


