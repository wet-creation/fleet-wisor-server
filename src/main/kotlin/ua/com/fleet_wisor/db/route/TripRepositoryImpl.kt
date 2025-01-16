package ua.com.fleet_wisor.db.trip

import org.ktorm.dsl.*
import ua.com.fleet_wisor.db.transactionalQuery
import ua.com.fleet_wisor.models.trip.Trip
import ua.com.fleet_wisor.models.trip.TripCreate
import ua.com.fleet_wisor.models.trip.TripRepository

class TripRepositoryImpl : TripRepository {
    override suspend fun all(): List<Trip> {
        return transactionalQuery { database ->
            database.from(TripTable).select().map { it.toTrip() }
        }
    }

    override suspend fun findById(id: Int): Trip? {
        return transactionalQuery { database ->
            database.from(TripTable).select().where { TripTable.id eq id }.map { it.toTrip() }.firstOrNull()
        }
    }

    override suspend fun update(trip: Trip): Trip? {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: Int): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun create(trip: TripCreate) {
        transactionalQuery { database ->
            database.insert(TripTable) {
                set(TripTable.name, trip.name)
                set(TripTable.description, trip.description)
                set(TripTable.latitudeStart, trip.positionStart.latitude)
                set(TripTable.longitudeStart, trip.positionStart.longitude)
                set(TripTable.latitudeEnd, trip.positionEnd.longitude)
                set(TripTable.longitudeEnd, trip.positionEnd.longitude)
            }
        }
    }
}