package ua.com.fleet_wisor

import io.ktor.server.application.*
import ua.com.fleet_wisor.db.DatabaseFactory
import ua.com.fleet_wisor.db.car.CarRepositoryImpl
import ua.com.fleet_wisor.db.trip.TripRepositoryImpl
import ua.com.fleet_wisor.db.user.UserRepositoryImpl
import ua.com.fleet_wisor.db.user.driver.DriverRepositoryImpl
import ua.com.fleet_wisor.di.configureFrameworks
import ua.com.fleet_wisor.minio.MinioService
import ua.com.fleet_wisor.plugins.configureAuth
import ua.com.fleet_wisor.plugins.configureContentNegotiation
import ua.com.fleet_wisor.plugins.configureStatusPages
import ua.com.fleet_wisor.routes.configureRouting

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureAuth()
    configureContentNegotiation()
    configureFrameworks()
    DatabaseFactory.init()
    MinioService.init()
    configureStatusPages()
    configureRouting(
        userRepository = UserRepositoryImpl(),
        driverRepository = DriverRepositoryImpl(),
        carRepository = CarRepositoryImpl(),
        tripRepository = TripRepositoryImpl(),
    )
}
