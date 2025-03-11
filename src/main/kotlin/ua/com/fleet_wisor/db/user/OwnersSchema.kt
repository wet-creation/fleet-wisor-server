package ua.com.fleet_wisor.db.user

import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.*
import ua.com.fleet_wisor.models.user.Owner


object OwnerTable : Table<Nothing>("owner") {
    val id = int("id").primaryKey()
    var email = varchar("email")
    var name = varchar("name")
    var surname = varchar("surname")
    var password = varchar("password")
}


fun QueryRowSet.toUser(): Owner {
    val t = this
    return Owner(
        email = t[OwnerTable.email]!!,
        id = t[OwnerTable.id]!!,
        password = t[OwnerTable.password]!!,
        name = t[OwnerTable.name]!!,
        surname = t[OwnerTable.surname]!!,
    )
}


