package ua.com.fleet_wisor.db.user

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import ua.com.fleet_wisor.db.suspendTransaction
import ua.com.fleet_wisor.models.user.Role
import ua.com.fleet_wisor.models.user.User
import ua.com.fleet_wisor.models.user.UserCreate
import ua.com.fleet_wisor.models.user.UserRepository

class UserRepositoryImpl : UserRepository {

    override suspend fun findByEmail(email: String): User? = suspendTransaction {
        UserDao.find { UserTable.email eq email }.limit(1).map { it.toModel() }.firstOrNull()
    }

    override suspend fun all(): List<User> = suspendTransaction {
        UserDao.all().map { it.toModel() }
    }

    override suspend fun findById(id: Int): User? = suspendTransaction {
        UserDao.findById(id)?.toModel()

    }

    override suspend fun findByRole(role: Role): List<User> = suspendTransaction {
        UserDao.find { UserTable.role eq role }.limit(1).map { it.toModel() }
    }

    override suspend fun update(id: Int, user: User): User? = suspendTransaction {
        UserDao.findByIdAndUpdate(id) {
            it.email = user.email
            it.role = user.role
            it.password = user.password
        }?.toModel()
    }

    override suspend fun delete(id: Int): Boolean = suspendTransaction {
        val rowsDeleted = UserTable.deleteWhere {
            UserTable.id eq id
        }
        rowsDeleted == 1
    }

    override suspend fun create(user: UserCreate): Unit = suspendTransaction {
        UserDao.new {
            email = user.email
            password = user.password
            role = user.role
        }
    }
}