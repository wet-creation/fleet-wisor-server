package ua.com.fleet_wisor.models.car

import ua.com.fleet_wisor.routes.car.dto.CarFillUpDto


data class CarFillUp(
    val id: Int,
    val time: String,
    val price: Double,
    val amount: Double,
    val checkUrl: String,
    val fuelUnits: FuelUnits,
    val fuelType: FuelType,
    val car: Car
) {
    fun asCarFillUpDto(lang: String) = CarFillUpDto(
        id = id,
        time = time,
        price = price,
        amount = amount,
        checkUrl = checkUrl,
        fuelType = fuelType.asFuelTypeDto(lang),
        unit = fuelUnits.asFuelUnits(lang),
        car = car.asCarDto(lang)
    )
}



