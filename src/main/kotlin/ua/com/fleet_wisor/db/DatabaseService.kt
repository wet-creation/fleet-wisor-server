package ua.com.fleet_wisor.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.ktorm.database.Database
import org.ktorm.dsl.Query
import org.ktorm.dsl.QueryRowSet
import org.ktorm.dsl.forEach
import org.ktorm.schema.Column
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
            .baselineOnMigrate(true)
            .load()


        flyway.migrate()
    }

    private fun hikariConfig(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "com.mysql.cj.jdbc.Driver"
        config.jdbcUrl = dbUrl
        config.maximumPoolSize = 5
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
fun <T> useConnection(block: (Database) -> T): T {
    return DatabaseFactory.database.useConnection { block(DatabaseFactory.database) }
}


fun <K : Any, V> Query.mapCollection(
    pkColumn: Column<K>,
    merge: (existing: V, newItem: V) -> V,
    transform: (QueryRowSet) -> V
): List<V> {
    val map = mutableMapOf<K, V>()

    this.forEach { row ->
        val id = row[pkColumn]!!
        val newItem = transform(row)

        map[id] = map[id]?.let { existing -> merge(existing, newItem) } ?: newItem
    }

    return map.values.toList()
}
