package ua.com.fleet_wisor.models.car

import ua.com.fleet_wisor.models.driver.Driver
import ua.com.fleet_wisor.models.user.Owner
import ua.com.fleet_wisor.routes.car.dto.CarBodyDto
import ua.com.fleet_wisor.routes.car.dto.CarDto
import ua.com.fleet_wisor.routes.car.dto.FuelTypeDto

data class CarBody(val id: Int = -1, val nameUk: String = "", val nameEn: String = "") {
    fun asCarBodyDto(): CarBodyDto {
        return CarBodyDto(id = id, name = nameUk)
    }
}

data class FuelType(val id: Int = -1, val nameUk: String = "", val nameEn: String = "") {
    fun asFuelTypeDto(): FuelTypeDto {
        return FuelTypeDto(id, nameUk)
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
    val drivers: List<Driver> = listOf(),
    val fuelTypes: List<FuelType> = listOf(),
    val carBody: CarBody = CarBody(),
) {
    fun asCarDto() = CarDto(
        id = id,
        brandName = brandName,
        color = color,
        vin = vin,
        model = model,
        licensePlate = licensePlate,
        mileAge = mileAge,
        owner = owner,
        drivers = drivers,
        fuelTypes = fuelTypes.map { it.asFuelTypeDto() },
        carBody = carBody.asCarBodyDto()
    )
}


