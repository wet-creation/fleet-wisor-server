package ua.com.fleet_wisor.db.user.driver

import ua.com.fleet_wisor.db.suspendTransaction
import ua.com.fleet_wisor.db.user.UserDao
import ua.com.fleet_wisor.db.user.UserTable
import ua.com.fleet_wisor.models.user.Role
import ua.com.fleet_wisor.models.user.driver.Driver
import ua.com.fleet_wisor.models.user.driver.DriverCreate
import ua.com.fleet_wisor.models.user.driver.DriverRepository

class DriverRepositoryImpl: DriverRepository {
    override suspend fun findByEmail(email: String): Driver? = suspendTransaction {
        DriverDao.find { UserTable.email eq email }.limit(1).map { it.toModel() }.firstOrNull()

    }

    override suspend fun all(): List<Driver> = suspendTransaction {
        DriverDao.all().map { it.toModel() }

    }

    override suspend fun findById(id: Int): Driver? = suspendTransaction {
        DriverDao.find { DriverTable.userId eq id }.firstOrNull()?.toModel()

    }

    override suspend fun update(driver: Driver): Driver? = suspendTransaction {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: Int): Boolean = suspendTransaction {
        TODO("Not yet implemented")
    }

    override suspend fun create(driver: DriverCreate): Unit = suspendTransaction {
        val user = UserDao.new {
            email = driver.email
            password = driver.password
            role = Role.OWNER
        }
        DriverDao.new {
            this.user = user
            surname = driver.surname
            name = driver.name
            phone = driver.phone
            driverLicenseNumber = driver.driverLicenseNumber
            uniqueCode = driver.uniqueCode
        }
    }
}