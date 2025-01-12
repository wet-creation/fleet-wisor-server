package ua.com.fleet_wisor.db.user.driver

import org.ktorm.dsl.*
import ua.com.fleet_wisor.db.transactionalQuery
import ua.com.fleet_wisor.db.user.UserTable
import ua.com.fleet_wisor.models.user.Role
import ua.com.fleet_wisor.models.user.driver.Driver
import ua.com.fleet_wisor.models.user.driver.DriverCreate
import ua.com.fleet_wisor.models.user.driver.DriverRepository

class DriverRepositoryImpl : DriverRepository {
    override suspend fun findByEmail(email: String): Driver? {
        return transactionalQuery { database ->
            database.from(UserTable).innerJoin(DriverTable, on = DriverTable.userId eq UserTable.id).select()
                .where { UserTable.email eq email }.map {
                    it.toDriver()
                }.firstOrNull()
        }

    }

    override suspend fun all(): List<Driver> {
        return transactionalQuery { database ->
            database.from(UserTable).innerJoin(DriverTable, on = DriverTable.userId eq UserTable.id).select()
                .map { it.toDriver() }

        }

    }

    override suspend fun findById(id: Int): Driver? {
        return transactionalQuery { database ->
            database.from(UserTable).innerJoin(DriverTable, on = DriverTable.userId eq UserTable.id).select()
                .where { UserTable.id eq id }.map {
                    it.toDriver()
                }.firstOrNull()
        }

    }

    override suspend fun update(driver: Driver): Driver? {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: Int): Boolean  {
        TODO("Not yet implemented")
    }

    override suspend fun create(driver: DriverCreate) {
        transactionalQuery { database ->
           val id = database.insertAndGenerateKey(UserTable) {
                set(it.email, driver.email)
                set(it.name, driver.name)
                set(it.surname, driver.surname)
                set(it.password, driver.password)
                set(it.role, Role.DRIVER)
            } as Int

            database.insert(DriverTable) {
                set(it.userId, id)
                set(it.phone, driver.phone)
                set(it.uniqueCode, driver.uniqueCode)
                set(it.driverLicenseNumber, driver.driverLicenseNumber)
            }
        }
    }
}