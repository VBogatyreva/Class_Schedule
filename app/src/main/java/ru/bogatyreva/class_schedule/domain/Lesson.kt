package ru.bogatyreva.class_schedule.domain

import java.time.Instant

data class Lesson (
    val id: Int = 0,
    val pairNumber: Int,                // Номер пары (1, 2, 3...)
    val startTime: String,              // "8:00" - для UI
    val endTime: String,                // "8:45" - для UI
    val discipline: String,             // "Философия"
    val audience: String,               // "Каб. 209"
    val teacher: String,                // "Иванова М.И."
    val date: Instant                   // Дата занятия
)

enum class Semester {
    FALL,                               // Осенний семестр
    SPRING                              // Весенний семестр
}
