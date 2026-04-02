package ru.bogatyreva.class_schedule.utils

// Функция для получения инициалов имени и отчества (только для полного имени)
fun getInitials(fullName: String): String {
    if (fullName.isBlank()) return ""

    val parts = fullName.trim().split(" ")

    // Только если ровно 3 части (Фамилия + Имя + Отчество)
    return if (parts.size == 3) {
        "${parts[1].first()}${parts[2].first()}".uppercase()
    } else {
        "" // Если имя не полное — ничего не выводим
    }
}