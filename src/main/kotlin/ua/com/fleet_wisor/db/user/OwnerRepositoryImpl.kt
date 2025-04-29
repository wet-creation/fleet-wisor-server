package ua.com.fleet_wisor.db.user

import org.ktorm.dsl.*
import org.ktorm.support.mysql.bulkInsertOrUpdate
import ua.com.fleet_wisor.db.car.FuelTypeTable
import ua.com.fleet_wisor.db.transactionalQuery
import ua.com.fleet_wisor.db.useConnection
import ua.com.fleet_wisor.models.user.*
import ua.com.fleet_wisor.routes.owner.dtos.OwnerSettingsUpsert

class OwnerRepositoryImpl : OwnerRepository {


    override suspend fun findByEmail(email: String): Owner? {
        return useConnection { database ->
            database.from(OwnerTable).select().where { OwnerTable.email eq email }.map {
                it.toUser()
            }.firstOrNull()
        }
    }

    override suspend fun getOwnerSettings(ownerId: Int): OwnerSettings {
        return useConnection { database ->
            val fuelUnits =
                database.from(OwnerUnitTable).innerJoin(FuelUnitsTable, FuelUnitsTable.id eq OwnerUnitTable.unitId)
                    .innerJoin(FuelTypeTable, FuelTypeTable.id eq OwnerUnitTable.fuelTypeId)
                    .select()
                    .where { OwnerUnitTable.ownerId eq ownerId }.map {
                        it.toFuelUnits()
                    }
            OwnerSettings(
                fuelUnits = fuelUnits
            )
        }
    }

    override suspend fun setOwnerSettings(ownerSettings: OwnerSettingsUpsert, ownerId: Int) {
        transactionalQuery { database ->
            database.bulkInsertOrUpdate(OwnerUnitTable) {
                ownerSettings.fuelUnits.forEach { unit ->
                    item {
                        set(OwnerUnitTable.ownerId, ownerId)
                        set(OwnerUnitTable.unitId, unit.idUnit)
                        set(OwnerUnitTable.fuelTypeId, unit.idFuelType)
                    }
                }
                onDuplicateKey { duplicateKey ->
                    set(duplicateKey.unitId, values(duplicateKey.unitId))
                }

            }
        }
    }

    override suspend fun all(): List<Owner> {
        return useConnection { database ->
            database.from(OwnerTable).select().map { it.toUser() }
        }
    }

    override suspend fun findById(id: Int): Owner? {
        return useConnection { database ->
            database.from(OwnerTable).select().where { OwnerTable.id eq id }.map { it.toUser() }.firstOrNull()
        }
    }

    override suspend fun updatePassword(ownerId: Int, newPassword: String) {
        return transactionalQuery { database ->
            database.update(OwnerTable) {
                set(it.password, newPassword)
                where { OwnerTable.id eq ownerId }
            }

        }
    }


    override suspend fun updateInfo(owner: OwnerNoPassword): OwnerNoPassword? {
        return transactionalQuery { database ->
            database.update(OwnerTable) {
                set(it.name, owner.name)
                set(it.surname, owner.surname)
                set(it.email, owner.email)
                where { OwnerTable.id eq owner.id }
            }

            database.from(OwnerTable).select().where { OwnerTable.id eq owner.id }
                .map { it.toUser().asOwnerNoPassword() }
                .firstOrNull()
        }
    }

    override suspend fun delete(id: Int): Boolean {
        return transactionalQuery { database ->
            database.delete(OwnerTable) { it.id eq id }
        } == 1
    }

    override suspend fun create(owner: OwnerCreate) {
        transactionalQuery { database ->
            database.insertAndGenerateKey(OwnerTable) {
                set(it.email, owner.email)
                set(it.name, owner.name)
                set(it.surname, owner.surname)
                set(it.password, owner.password)
            }
        }
    }
}