package ua.com.fleet_wisor.db.user

import org.ktorm.dsl.*
import ua.com.fleet_wisor.db.transactionalQuery
import ua.com.fleet_wisor.models.user.*

class OwnerRepositoryImpl : OwnerRepository {

    override suspend fun findByEmail(email: String): Owner? {
        return transactionalQuery { database ->
            database.from(UserTable).select().where { UserTable.email eq email }.map {
                it.toUser().asOwner()
            }.firstOrNull()
        }
    }

    override suspend fun all(): List<Owner> {
        return transactionalQuery { database ->
            database.from(UserTable).select().map { it.toUser().asOwner() }
        }
    }

    override suspend fun findById(id: Int): Owner? {
        return transactionalQuery { database ->
            database.from(UserTable).select().where { UserTable.id eq id }.map { it.toUser().asOwner() }.firstOrNull()
        }
    }

    override suspend fun findByRole(role: Role): List<Owner> {
        return transactionalQuery { database ->
            database.from(UserTable).select().where { UserTable.role eq role }.map { it.toUser().asOwner() }
        }
    }

    override suspend fun update(owner: Owner): Owner? {
        return transactionalQuery { database ->
            database.update(UserTable) {
                set(it.name, owner.surname)
                set(it.surname, owner.surname)
                where { UserTable.id eq owner.id }
            }

            database.from(UserTable).select().where { UserTable.id eq owner.id }.map { it.toUser().asOwner() }
                .firstOrNull()
        }
    }

    override suspend fun delete(id: Int): Boolean {
        return transactionalQuery { database ->
            database.delete(UserTable) { it.id eq id }
        } == 1
    }

    override suspend fun create(owner: OwnerCreate) {
        transactionalQuery { database ->
            database.insertAndGenerateKey(UserTable) {
                set(it.email, owner.email)
                set(it.name, owner.name)
                set(it.surname, owner.surname)
                set(it.password, owner.password)
                set(it.role, Role.OWNER)
            }
        }
    }
}