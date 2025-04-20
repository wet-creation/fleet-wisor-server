package ua.com.fleet_wisor

import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.request.*
import org.slf4j.event.Level
import ua.com.fleet_wisor.db.DatabaseFactory
import ua.com.fleet_wisor.db.car.CarRepositoryImpl
import ua.com.fleet_wisor.db.driver.DriverRepositoryImpl
import ua.com.fleet_wisor.db.reports.ReportsRepositoryImpl
import ua.com.fleet_wisor.db.user.OwnerRepositoryImpl
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
        ownerRepository = OwnerRepositoryImpl(),
        driverRepository = DriverRepositoryImpl(),
        carRepository = CarRepositoryImpl(),
        reportRepository = ReportsRepositoryImpl(),
    )
    install(CallLogging) {
        level = Level.WARN
        filter { call ->
            call.request.path().startsWith("/api/v1")
        }
        format { call ->
            val status = call.response.status()
            val httpMethod = call.request.httpMethod.value
            val userAgent = call.request.headers["User-Agent"]
            "Status: $status, HTTP method: $httpMethod, User agent: $userAgent"
        }
    }
}
