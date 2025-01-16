package ua.com.fleet_wisor.routes

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ua.com.fleet_wisor.models.user.User
import ua.com.fleet_wisor.models.user.UserCreate
import ua.com.fleet_wisor.models.user.UserRepository
import ua.com.fleet_wisor.models.user.hashPassword
import ua.com.fleet_wisor.utils.notFoundMessage

fun Route.configureOwnerRouting(
    userRepository: UserRepository
) {
    route("/owners") {
        post {
            val user = call.receive<UserCreate>()
            val hashPassword = hashPassword(user.password)
            val userWithHashPassword = UserCreate(
                email = user.email,
                password = hashPassword,
                role = user.role,
                name = user.name,
                surname = user.surname,
            )
            userRepository.create(userWithHashPassword)
            call.respond(HttpStatusCode.Created)
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val user = userRepository.findById(id)
            if (user != null) {
                call.respond(HttpStatusCode.OK, user)
            } else {
                throw NotFoundException(notFoundMessage(User::class, id, "Check your id"))
            }
        }
        authenticate {
            get("/me") {
                val id = call.principal<UserIdPrincipal>()?.name ?: throw IllegalArgumentException("Invalid")
                val user = userRepository.findById(
                    id.toIntOrNull() ?: throw NotFoundException(
                        notFoundMessage(
                            User::class,
                            id,
                            "User not found"
                        )
                    )
                )?: throw NotFoundException(notFoundMessage(User::class, id, "User not found"))

                call.respond(HttpStatusCode.OK, user)

            }

        }
        get {
            val user = userRepository.all()
            call.respond(HttpStatusCode.OK, user)
        }

        put {
            val user = call.receive<User>()
            val res = userRepository.update(user)
                ?: throw NotFoundException(notFoundMessage(User::class, user.id, "Check your id"))
            call.respond(HttpStatusCode.OK, res)
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            if (userRepository.delete(id))
                call.respond(HttpStatusCode.OK)
            else throw NotFoundException(notFoundMessage(User::class, id, "Check your id"))
        }
    }
}
