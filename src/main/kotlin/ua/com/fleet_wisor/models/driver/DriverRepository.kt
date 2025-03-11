package ua.com.fleet_wisor.models.driver

interface DriverRepository {

    suspend fun findByPhone(phone: String): Driver?
    suspend fun all(): List<Driver>
    suspend fun findById(id: Int): Driver?
    suspend fun update(driver: Driver): Driver?
    suspend fun delete(id: Int): Boolean
    suspend fun create(driver: DriverCreate)

    suspend fun assignCar(driverWithCarCreate: DriverWithCarCreate)
    suspend fun driverWithCars(): List<DriverWithCars>
}