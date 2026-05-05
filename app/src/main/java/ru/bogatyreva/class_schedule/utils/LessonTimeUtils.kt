package ru.bogatyreva.class_schedule.utils

import ru.bogatyreva.class_schedule.domain.model.Lesson
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.LocalDate
import java.time.ZoneId

object LessonTimeUtils {

    private val timeFormatter = DateTimeFormatter.ofPattern("H:mm")

    // Определяет, какой урок должен быть подсвечен
    fun getCurrentOrNextLessonId(
        lessons: List<Lesson>,
        currentTime: LocalTime = LocalTime.now(),
        currentDate: LocalDate = LocalDate.now()
    ): Int? {
        if (lessons.isEmpty()) return null

        // Фильтруем уроки только для текущей даты
        val todayLessons = lessons.filter { lesson ->
            val lessonDate = lesson.date.atZone(ZoneId.systemDefault()).toLocalDate()
            lessonDate == currentDate
        }.sortedBy { it.pairNumber }

        if (todayLessons.isEmpty()) return null

        for (i in todayLessons.indices) {
            val lesson = todayLessons[i]
            val lessonStart = LocalTime.parse(lesson.startTime, timeFormatter)
            val lessonEnd = LocalTime.parse(lesson.endTime, timeFormatter)

            // Текущий урок идет прямо сейчас
            if (currentTime in lessonStart..lessonEnd) {
                return lesson.id
            }

            // Урок еще не начался
            if (currentTime < lessonStart) {
                // Проверяем, не наступило ли время следующего урока
                return lesson.id
            }
        }

        // Все уроки уже прошли
        return null
    }

    // Определяет, нужно ли подсветить конкретный урок
    fun isLessonHighlighted(
        lesson: Lesson,
        lessons: List<Lesson>,
        currentTime: LocalTime = LocalTime.now(),
        currentDate: LocalDate = LocalDate.now()
    ): Boolean {
        val lessonDate = lesson.date.atZone(ZoneId.systemDefault()).toLocalDate()
        if (lessonDate != currentDate) return false

        val currentOrNextId = getCurrentOrNextLessonId(lessons, currentTime, currentDate)
        return currentOrNextId == lesson.id
    }
}