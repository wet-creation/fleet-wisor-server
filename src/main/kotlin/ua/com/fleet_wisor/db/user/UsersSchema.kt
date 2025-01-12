package ua.com.fleet_wisor.db.user

import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.Table
import org.ktorm.schema.enum
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import ua.com.fleet_wisor.models.user.Role
import ua.com.fleet_wisor.models.user.User


object UserTable : Table<Nothing>("user") {
    val id = int("id").primaryKey()
    var email = varchar("email")
    var name = varchar("name")
    var surname = varchar("surname")
    var password = varchar("password")
    var role = enum<Role>("role")
}

fun QueryRowSet.toUser(): User {
    val t = this
    return User(
        email = t[UserTable.email]!!,
        id = t[UserTable.id]!!,
        password = t[UserTable.password]!!,
        role = t[UserTable.role]!!,
        name = t[UserTable.name]!!,
        surname = t[UserTable.surname]!!,
    )
}


