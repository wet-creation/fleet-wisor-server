package ua.com.fleet_wisor.models.user.driver

interface DriverRepository {

    suspend fun findByEmail(email: String): Driver?
    suspend fun all(): List<Driver>
    suspend fun findById(id: Int): Driver?
    suspend fun update(driver: Driver): Driver?
    suspend fun delete(id: Int): Boolean
    suspend fun create(driver: DriverCreate)

    suspend fun assignCar(driverWithCarCreate: DriverWithCarCreate)
}