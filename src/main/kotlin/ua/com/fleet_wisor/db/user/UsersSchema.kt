package ua.com.fleet_wisor.db.user

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import ua.com.fleet_wisor.models.user.Role
import ua.com.fleet_wisor.models.user.User


object UserTable : IntIdTable("user") {
    val email = varchar("email", 255).uniqueIndex()
    val password = varchar("password", 255)
    val role = customEnumeration(
        "role",
        "ENUM('OWNER', 'DRIVER')",
        { value -> Role.valueOf(value as String) },
        { it.name })

}

class UserDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserDao>(UserTable)
    var email by UserTable.email
    var password by UserTable.password
    var role by UserTable.role
}


fun UserDao.toModel() = User(
    id = id.value,
    email = email,
    password = password,
    role = Role.valueOf(role.name),
)

