package ua.com.fleet_wisor.models.user

interface UserRepository {
    suspend fun findByEmail(email: String): User?
    suspend fun all(): List<User>
    suspend fun findById(id: Int): User?
    suspend fun findByRole(role: Role): List<User>
    suspend fun update(owner: User): User?
    suspend fun delete(id: Int): Boolean
    suspend fun create(owner: UserCreate)

}