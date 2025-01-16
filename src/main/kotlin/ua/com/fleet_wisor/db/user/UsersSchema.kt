package ua.com.fleet_wisor.db.user

import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.*
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

object TokenTable : Table<Nothing>("token") {
    val userId = int("user_id")
    val refresh = varchar("refresh")
    val modifiedAt = long("modifiedAt")
    val expiredAt = long("expiredAt")
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


