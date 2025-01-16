package ua.com.fleet_wisor.models.trip

interface TripRepository {
    suspend fun all(): List<Trip>
    suspend fun findById(id: Int): Trip?
    suspend fun update(trip: Trip): Trip?
    suspend fun delete(id: Int): Boolean
    suspend fun create(trip: TripCreate)

}