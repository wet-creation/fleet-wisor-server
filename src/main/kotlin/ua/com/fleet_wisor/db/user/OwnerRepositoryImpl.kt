package ua.com.fleet_wisor.db.user

import org.ktorm.dsl.*
import ua.com.fleet_wisor.db.transactionalQuery
import ua.com.fleet_wisor.db.useConnection
import ua.com.fleet_wisor.models.user.*

class OwnerRepositoryImpl : OwnerRepository {

    override suspend fun findByEmail(email: String): Owner? {
        return useConnection { database ->
            database.from(OwnerTable).select().where { OwnerTable.email eq email }.map {
                it.toUser()
            }.firstOrNull()
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


    override suspend fun update(owner: Owner): Owner? {
        return transactionalQuery { database ->
            database.update(OwnerTable) {
                set(it.name, owner.surname)
                set(it.surname, owner.surname)
                where { OwnerTable.id eq owner.id }
            }

            database.from(OwnerTable).select().where { OwnerTable.id eq owner.id }.map { it.toUser() }
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