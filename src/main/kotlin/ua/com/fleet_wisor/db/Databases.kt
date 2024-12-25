package ua.com.fleet_wisor.db

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ua.com.fleet_wisor.models.user.User
import ua.com.fleet_wisor.models.user.UserCreate
import ua.com.fleet_wisor.models.user.UserRepository

fun Application.configureDatabases(
    userRepository: UserRepository
) {
    val database = Database.connect(
        url = "jdbc:h2:mem:fleetwisor;DB_CLOSE_DELAY=-1",
        user = "root",
        driver = "org.h2.Driver",
        password = "",
    )
    routing {
        post("/users") {
            val user = call.receive<UserCreate>()
            val id = userRepository.create(user)
            call.respond(HttpStatusCode.Created, id)
        }
        
        get("/users/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val user = userRepository.findById(id)
            if (user != null) {
                call.respond(HttpStatusCode.OK, user)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        
        put("/users/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val user = call.receive<User>()
            userRepository.update(id, user)
            call.respond(HttpStatusCode.OK)
        }
        
        delete("/users/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            userRepository.delete(id)
            call.respond(HttpStatusCode.OK)
        }
    }
}


suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)