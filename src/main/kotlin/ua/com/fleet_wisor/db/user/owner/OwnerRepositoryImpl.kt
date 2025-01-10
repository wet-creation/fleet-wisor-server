package ua.com.fleet_wisor.db.user.owner

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import ua.com.fleet_wisor.db.suspendTransaction
import ua.com.fleet_wisor.db.user.UserDao
import ua.com.fleet_wisor.db.user.UserTable
import ua.com.fleet_wisor.models.user.Role
import ua.com.fleet_wisor.models.user.owner.Owner
import ua.com.fleet_wisor.models.user.owner.OwnerCreate
import ua.com.fleet_wisor.models.user.owner.OwnerRepository

class OwnerRepositoryImpl : OwnerRepository {
    override suspend fun findByEmail(email: String): Owner? = suspendTransaction {
        OwnerDao.find { UserTable.email eq email }.limit(1).map { it.toModel() }.firstOrNull()
    }

    override suspend fun all(): List<Owner> = suspendTransaction {
        OwnerDao.all().map { it.toModel() }
    }

    override suspend fun findById(id: Int): Owner? = suspendTransaction {
        OwnerDao.findById(id)?.toModel()
    }

    override suspend fun update(owner: Owner): Owner? = suspendTransaction {
        OwnerDao.findSingleByAndUpdate(OwnerTable.id eq owner.id) {
            it.name = owner.name
            it.surname = owner.surname
        }?.toModel()

    }

    override suspend fun delete(id: Int): Boolean = suspendTransaction {
        val rowsDeleted = UserTable.deleteWhere {
            UserTable.id eq id
        }
        rowsDeleted == 1
    }

    override suspend fun create(owner: OwnerCreate): Unit = suspendTransaction {
        val user = UserDao.new {
            email = owner.email
            password = owner.password
            role = Role.OWNER
        }
        OwnerDao.new {
            this.user = user
            surname = owner.surname
            name = owner.name
        }
    }
}