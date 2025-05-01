package ua.com.fleet_wisor.utils

fun getConfig(key: String): String {
    val appConfig = System.getenv()
    return appConfig[key] ?: throw IllegalArgumentException("$key was not provided, check env")
}