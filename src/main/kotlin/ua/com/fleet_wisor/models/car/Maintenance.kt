package ua.com.fleet_wisor.models.car

import ua.com.fleet_wisor.routes.car.dto.MaintenanceDto


data class Maintenance(
    val id: Int,
    val time: String,
    val description: String,
    val checkUrl: String,
    val car: Car,
    val price: Double,
) {
    fun asMaintenanceDto(lang: String): MaintenanceDto {
        return MaintenanceDto(
            id = id,
            time = time,
            description = description,
            checkUrl = checkUrl,
            car = car.asCarDto(lang),
            price = price
        )
    }
}
