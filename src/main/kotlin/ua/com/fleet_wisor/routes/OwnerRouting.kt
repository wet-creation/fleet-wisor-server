package ua.com.fleet_wisor.routes

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ua.com.fleet_wisor.models.user.Owner
import ua.com.fleet_wisor.models.user.OwnerCreate
import ua.com.fleet_wisor.models.user.OwnerRepository
import ua.com.fleet_wisor.models.user.hashPassword
import ua.com.fleet_wisor.utils.notFoundMessage

fun Route.configureOwnerRouting(
    ownerRepository: OwnerRepository
) {
    route("/owners") {
        post {
            val user = call.receive<OwnerCreate>()
            val hashPassword = hashPassword(user.password)
            val userWithHashPassword = OwnerCreate(
                email = user.email,
                password = hashPassword,
                name = user.name,
                surname = user.surname,
            )
            ownerRepository.create(userWithHashPassword)
            call.respond(HttpStatusCode.Created)
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val user = ownerRepository.findById(id)
            if (user != null) {
                call.respond(HttpStatusCode.OK, user)
            } else {
                throw NotFoundException(notFoundMessage(Owner::class, id, "Check your id"))
            }
        }
        authenticate {
            get("/me") {
                val id = call.principal<UserIdPrincipal>()?.name ?: throw IllegalArgumentException("Invalid")
                val owner = ownerRepository.findById(
                    id.toIntOrNull() ?: throw NotFoundException(
                        notFoundMessage(
                            Owner::class,
                            id,
                            "User not found"
                        )
                    )
                )?: throw NotFoundException(notFoundMessage(Owner::class, id, "User not found"))

                call.respond(HttpStatusCode.OK, owner)

            }

        }
        get {
            val user = ownerRepository.all()
            call.respond(HttpStatusCode.OK, user)
        }

        put {
            val owner = call.receive<Owner>()
            val res = ownerRepository.update(owner)
                ?: throw NotFoundException(notFoundMessage(Owner::class, owner.id, "Check your id"))
            call.respond(HttpStatusCode.OK, res)
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            if (ownerRepository.delete(id))
                call.respond(HttpStatusCode.OK)
            else throw NotFoundException(notFoundMessage(Owner::class, id, "Check your id"))
        }
    }
}
