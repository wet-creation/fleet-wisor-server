package ua.com.fleet_wisor.models.car

import ua.com.fleet_wisor.routes.car.dto.CarFillUpDto


data class CarFillUp(
    val id: Int,
    val time: String,
    val price: Double,
    val amount: Double,
    val checkUrl: String,
    val car: Car
) {
    fun asCarFillUpDto() = CarFillUpDto(
        id = id,
        time = time,
        price = price,
        amount = amount,
        checkUrl = checkUrl,
        car = car.asCarDto()
    )
}



