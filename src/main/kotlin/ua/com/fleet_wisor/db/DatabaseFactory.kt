package ua.com.fleet_wisor.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import ua.com.fleet_wisor.db.car.CarBodyTable
import ua.com.fleet_wisor.db.car.CarTable
import ua.com.fleet_wisor.db.car.FuelTypeTable
import ua.com.fleet_wisor.db.user.UserTable
import ua.com.fleet_wisor.db.user.driver.DriverTable
import ua.com.fleet_wisor.db.user.driver.DriverWithCarTable
import ua.com.fleet_wisor.db.user.owner.OwnerTable

object DatabaseFactory {

    private val appConfig = System.getenv()
    private val dbUrl = appConfig["DB_URL"]
    private val dbUser = appConfig["DB_USER"]
    private val dbPassword = appConfig["DB_PASSWORD"]
    fun init() {
        println("Initializing DB...")
        println("DB_URL: $dbUrl")

        val database = Database.connect(
            hikariConfig()
        )
        transaction(database) {
            SchemaUtils.create(
                UserTable,
                OwnerTable,
                DriverTable,
                CarBodyTable,
                FuelTypeTable,
                CarTable,
                DriverWithCarTable
            )
        }
//        val flyway = Flyway.configure()
//            .dataSource(dbUrl, dbUser, dbPassword)
//            .baselineOnMigrate(true)
//            .load()
//
//        flyway.migrate()
    }

    private fun hikariConfig(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "com.mysql.cj.jdbc.Driver"
        config.jdbcUrl = dbUrl
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.username = dbUser
        config.password = dbPassword
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        return HikariDataSource(config)
    }
}

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)