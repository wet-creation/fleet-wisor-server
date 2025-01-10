package ua.com.fleet_wisor.db.user.owner

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import ua.com.fleet_wisor.db.user.UserDao
import ua.com.fleet_wisor.db.user.UserTable
import ua.com.fleet_wisor.models.user.owner.Owner

object OwnerTable : IdTable<Int>("owner") {
    override val id: Column<EntityID<Int>> = reference(
        "userId",
        UserTable.id,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    ).uniqueIndex()
    val name = varchar("name", 255)
    val surname = varchar("surname", 255)

}

class OwnerDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<OwnerDao>(OwnerTable)

    var user by UserDao referencedOn OwnerTable.id
    var name by OwnerTable.name
    var surname by OwnerTable.surname
}


fun OwnerDao.toModel(): Owner {
    return Owner(
        id = user.id.value,
        email = user.email,
        name = name,
        surname = surname
    )
}