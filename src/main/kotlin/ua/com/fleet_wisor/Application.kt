package ua.com.fleet_wisor

import io.ktor.server.application.*
import ua.com.fleet_wisor.db.configureDatabases
import ua.com.fleet_wisor.db.user.UserRepositoryImpl
import ua.com.fleet_wisor.di.configureFrameworks
import ua.com.fleet_wisor.plugins.configureContentNegotiation
import ua.com.fleet_wisor.routes.configureRouting

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureContentNegotiation()
    configureDatabases(userRepository = UserRepositoryImpl())
    configureFrameworks()
    configureRouting()
}
