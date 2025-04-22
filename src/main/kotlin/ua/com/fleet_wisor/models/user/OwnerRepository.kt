package ua.com.fleet_wisor.models.user

interface OwnerRepository {
    suspend fun findByEmail(email: String): Owner?
    suspend fun all(): List<Owner>
    suspend fun findById(id: Int): Owner?
    suspend fun updateInfo(owner: OwnerNoPassword): OwnerNoPassword?
    suspend fun updatePassword(ownerId: Int, newPassword: String)
    suspend fun delete(id: Int): Boolean
    suspend fun create(owner: OwnerCreate)

}