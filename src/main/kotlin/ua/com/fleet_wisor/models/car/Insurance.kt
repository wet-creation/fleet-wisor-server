package ua.com.fleet_wisor.models.car

import ua.com.fleet_wisor.routes.car.dto.InsuranceDto

data class Insurance(
    val id: Int,
    val startDate: String,
    val endDate: String,
    val photoUrl: String,
) {
    fun asInsuranceDto(): InsuranceDto {
        return InsuranceDto(
            id = id,
            startDate = startDate,
            endDate = endDate,
            photoUrl = photoUrl
        )
    }
}

