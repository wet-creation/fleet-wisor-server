package ua.com.fleet_wisor.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.ktorm.database.Database

object DatabaseFactory {
    private val appConfig = System.getenv()
    private val dbUrl = appConfig["DB_URL"]
    private val dbUser = appConfig["DB_USER"]
    private val dbPassword = appConfig["DB_PASSWORD"]
    lateinit var database: Database

    fun init() {
        println("Initializing DB...")
        println("DB_URL: $dbUrl")

        database = Database.connect(
            hikariConfig()
        )


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