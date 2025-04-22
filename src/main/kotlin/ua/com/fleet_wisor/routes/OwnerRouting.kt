package ua.com.fleet_wisor.routes

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ua.com.fleet_wisor.models.user.*
import ua.com.fleet_wisor.utils.notFoundMessage

fun Route.configureOwnerRouting(
    ownerRepository: OwnerRepository
) {
    route("/owners") {

        authenticate {
            get("/{id}") {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
                val user = ownerRepository.findById(id)
                if (user != null) {
                    call.respond(HttpStatusCode.OK, user)
                } else {
                    throw NotFoundException(notFoundMessage(Owner::class, id, "Check your id"))
                }
            }
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
                ) ?: throw NotFoundException(notFoundMessage(Owner::class, id, "User not found"))

                call.respond(HttpStatusCode.OK, owner)

            }
            get {
                val user = ownerRepository.all()
                call.respond(HttpStatusCode.OK, user)
            }
            route("/update") {
                put("/info") {
                    val owner = call.receive<OwnerNoPassword>()
                    val res = ownerRepository.updateInfo(owner)
                        ?: throw NotFoundException(notFoundMessage(Owner::class, owner.id, "Check your id"))
                    call.respond(HttpStatusCode.OK, res)
                }
                put("/password") {
                    val id = call.principal<UserIdPrincipal>()?.name?.toIntOrNull()
                        ?: throw IllegalArgumentException("Invalid")
                    val body = call.receive<PasswordUpdate>()

                    val owner = ownerRepository.findById(id)
                        ?: throw NotFoundException(notFoundMessage(Owner::class, id, "User not found"))

                    if (!verifyPassword(body.oldPassword, owner.password)) throw BadRequestException("Passwords do not match")

                    val res = ownerRepository.updatePassword(
                        ownerId = id,
                        newPassword = hashPassword(body.newPassword)
                    )
                    call.respond(HttpStatusCode.OK, res)
                }
            }


        }

    }
}
