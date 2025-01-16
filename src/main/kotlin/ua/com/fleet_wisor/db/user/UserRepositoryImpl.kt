package ua.com.fleet_wisor.db.user

import org.ktorm.dsl.*
import ua.com.fleet_wisor.db.transactionalQuery
import ua.com.fleet_wisor.models.user.*

class UserRepositoryImpl : UserRepository {

    override suspend fun findByEmail(email: String): User? {
        return transactionalQuery { database ->
            database.from(UserTable).select().where { UserTable.email eq email }.map {
                it.toUser()
            }.firstOrNull()
        }
    }

    override suspend fun all(): List<User> {
        return transactionalQuery { database ->
            database.from(UserTable).select().map { it.toUser() }
        }
    }

    override suspend fun findById(id: Int): User? {
        return transactionalQuery { database ->
            database.from(UserTable).select().where { UserTable.id eq id }.map { it.toUser() }.firstOrNull()
        }
    }

    override suspend fun findByRole(role: Role): List<User> {
        return transactionalQuery { database ->
            database.from(UserTable).select().where { UserTable.role eq role }.map { it.toUser() }
        }
    }

    override suspend fun update(user: User): User? {
        return transactionalQuery { database ->
            database.update(UserTable) {
                set(it.name, user.surname)
                set(it.surname, user.surname)
                where { UserTable.id eq user.id }
            }

            database.from(UserTable).select().where { UserTable.id eq user.id }.map { it.toUser() }
                .firstOrNull()
        }
    }

    override suspend fun delete(id: Int): Boolean {
        return transactionalQuery { database ->
            database.delete(UserTable) { it.id eq id }
        } == 1
    }

    override suspend fun create(user: UserCreate) {
        transactionalQuery { database ->
            database.insertAndGenerateKey(UserTable) {
                set(it.email, user.email)
                set(it.name, user.name)
                set(it.surname, user.surname)
                set(it.password, user.password)
                set(it.role, user.role)
            }
        }
    }
}