package ua.com.fleet_wisor.models.reports

import kotlinx.serialization.Serializable

@Serializable
data class CarPeriodReport(
    val id: Int,
    val color: String?,
    val brandName: String?,
    val model: String?,
    val fillUpCount: Int,
    val totalFillUp: Double,
    val maintenanceCount: Int,
    val totalMaintenance: Double
)