package ua.com.fleet_wisor

import io.ktor.server.application.*
import ua.com.fleet_wisor.db.DatabaseFactory
import ua.com.fleet_wisor.db.car.CarRepositoryImpl
import ua.com.fleet_wisor.db.user.OwnerRepositoryImpl
import ua.com.fleet_wisor.db.user.driver.DriverRepositoryImpl
import ua.com.fleet_wisor.di.configureFrameworks
import ua.com.fleet_wisor.plugins.configureContentNegotiation
import ua.com.fleet_wisor.plugins.configureStatusPages
import ua.com.fleet_wisor.routes.configureRouting

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureContentNegotiation()
    configureFrameworks()
    DatabaseFactory.init()
    configureStatusPages()
    configureRouting(
        ownerRepository = OwnerRepositoryImpl(),
        driverRepository = DriverRepositoryImpl(),
        carRepository = CarRepositoryImpl(),
    )
}
