package ua.com.fleet_wisor.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.ktorm.database.Database
import ua.com.fleet_wisor.utils.getConfig

object DatabaseFactory {
    private val dbUrl = getConfig("DB_URL")
    private val dbUser = getConfig("DB_USER")
    private val dbPassword = getConfig("DB_PASSWORD")
    val database: Database = Database.connect(
        hikariConfig()
    )

    fun init() {
        println("Initializing DB...")
        println("DB_URL: $dbUrl")


        val flyway = Flyway.configure()
            .dataSource(dbUrl, dbUser, dbPassword)
            .load()

        flyway.migrate()
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


fun <T> transactionalQuery(block: (Database) -> T): T {
    return DatabaseFactory.database.useTransaction { block(DatabaseFactory.database) }
}