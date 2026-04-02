package ru.bogatyreva.class_schedule.domain

import ru.bogatyreva.class_schedule.domain.model.Lesson
import java.time.LocalTime
import java.time.format.DateTimeFormatter

//для получения времени окончания последнего занятия

class GetLastLessonEndTimeUseCase {

    operator fun invoke(lessons: List<Lesson>): String? {
        if (lessons.isEmpty()) return null

        // Берем последнее занятие по номеру пары
        val lastLesson = lessons.maxByOrNull { it.pairNumber } ?: return null

        // Возвращаем время его окончания
        return lastLesson.endTime
    }

    // Метод для получения времени окончания в минутах от начала дня
    fun getLastLessonEndTimeInMinutes(lessons: List<Lesson>): Int? {
        val endTime = invoke(lessons) ?: return null

        return try {
            val formatter = DateTimeFormatter.ofPattern("H:mm")
            val time = LocalTime.parse(endTime, formatter)
            time.hour * 60 + time.minute
        } catch (e: Exception) {
            null
        }
    }
}