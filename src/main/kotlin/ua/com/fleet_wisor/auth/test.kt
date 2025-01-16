package ua.com.fleet_wisor.auth

import ua.com.fleet_wisor.models.user.hashPassword

fun main() {
    println(hashPassword("password"))
}