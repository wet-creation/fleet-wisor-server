package ua.com.fleet_wisor.utils

import kotlin.reflect.KClass


fun notFoundMessage(classType: Any, withParam: Any, message: String = ""): String {
    val className = (classType as? KClass<*>)?.java?.simpleName ?: "Unknown"

    return "For $className with param $withParam nothing found. ${message.ifEmpty { "" }}"
}
