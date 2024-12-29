package ua.com.fleet_wisor.routes

import io.ktor.http.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ua.com.fleet_wisor.models.user.owner.Owner
import ua.com.fleet_wisor.models.user.owner.OwnerCreate
import ua.com.fleet_wisor.models.user.owner.OwnerRepository
import ua.com.fleet_wisor.utils.notFoundMessage

fun Route.configureOwnerRouting(
    ownerRepository: OwnerRepository
) {
    route("/owners") {
        post {
            val user = call.receive<OwnerCreate>()
            val id = ownerRepository.create(user)
            call.respond(HttpStatusCode.Created, id)
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val user = ownerRepository.findById(id)
            if (user != null) {
                call.respond(HttpStatusCode.OK, user.asOwnerRead())
            } else {
                throw NotFoundException(notFoundMessage(Owner::class, id, "Check your id"))
            }
        }
        get {
            val user = ownerRepository.all()
            call.respond(HttpStatusCode.OK, user.map { it.asOwnerRead() })
        }

        put {
            val user = call.receive<Owner>()
            val res = ownerRepository.update(user)
                ?: throw NotFoundException(notFoundMessage(Owner::class, user.id, "Check your id"))
            call.respond(HttpStatusCode.OK, res.asOwnerRead())
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            if (ownerRepository.delete(id))
                call.respond(HttpStatusCode.OK)
            else throw NotFoundException(notFoundMessage(Owner::class, id, "Check your id"))
        }
    }
}
