package ua.com.fleet_wisor.models.user

import ua.com.fleet_wisor.routes.owner.dtos.OwnerSettingsUpsert

interface OwnerRepository {
    suspend fun findByEmail(email: String): Owner?
    suspend fun getOwnerSettings(ownerId: Int): OwnerSettings?
    suspend fun setOwnerSettings(ownerSettings: OwnerSettingsUpsert, ownerId: Int)
    suspend fun all(): List<Owner>
    suspend fun findById(id: Int): Owner?
    suspend fun updateInfo(owner: OwnerNoPassword): OwnerNoPassword?
    suspend fun updatePassword(ownerId: Int, newPassword: String)
    suspend fun delete(id: Int): Boolean
    suspend fun create(owner: OwnerCreate)

}