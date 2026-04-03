package ru.bogatyreva.class_schedule.domain.model

import java.time.Duration
import java.time.Instant
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class Lesson (
    val id: Int = 0,
    val pairNumber: Int,                              // Номер пары (1, 2, 3...)
    val startTime: String,                            // "8:00" - для UI
    val endTime: String,                              // "8:45" - для UI
    val discipline: String,                           // "Философия"
    val audience: String? = null,                     // "209" или Онлайн
    val originalTeacher: String,                      // "Иванова М.И."
    val date: Instant,                                // Дата занятия
    val lessonType: LessonType = LessonType.UNKNOWN,  // Тип занятия
    val status: LessonStatus = LessonStatus.NORMAL,   // Статус занятия
    val isOnline: Boolean = false,                    // Онлайн или офлайн
    val onlineLink: String? = null,                   // Ссылка на онлайн-занятие
    val substitutionTeacher: String? = null,          // Преподаватель для замены
    val nextLessonStartTime: String? = null,           // Время начала следующего занятия (для расчета перерыва)
    val lessonMaterials: List<LessonMaterial> = emptyList(),  // Материалы к уроку
    val assignment: Assignment? = null                       // Задание к уроку
) {

    // Отображаемая аудитория
    val audienceDisplay: String
        get() = when {
            isOnline -> "Онлайн"
            audience != null -> "ауд. $audience"
            else -> "Не указано"
        }

    // Проверка, есть ли место проведения в аудитории
    val hasAudience: Boolean
        get() = !isOnline && audience != null && audience.isNotBlank()

    // Проверка, есть ли онлайн-ссылка
    val hasOnlineLink: Boolean
        get() = isOnline && !onlineLink.isNullOrBlank()

    // Отображаемое название типа занятия
    val lessonTypeDisplay: String
        get() = when (lessonType) {
            LessonType.LECTURE -> "лекция"
            LessonType.SEMINAR -> "семинар"
            LessonType.TEST -> "зачет"
            LessonType.EXAM -> "экзамен"
            LessonType.UNKNOWN -> ""
        }

    // Отображаемый статус занятия
    val statusDisplay: String?
        get() = when (status) {
            LessonStatus.CANCELLED -> "отменено"
            LessonStatus.SUBSTITUTION -> "замена"
            LessonStatus.NORMAL -> null
        }

    // Форматированное время занятия
    val formattedTime: String
        get() = "$startTime – $endTime"

    // Номер занятия для отображения
    val formattedPairNumber: String
        get() = "$pairNumber."

    // Проверка, является ли занятие онлайн
    val isOnlineLesson: Boolean
        get() = isOnline

    // Проверка, есть ли замена преподавателя
    val hasSubstitution: Boolean
        get() = status == LessonStatus.SUBSTITUTION

    // Проверка, отменено ли занятие
    val isCancelled: Boolean
        get() = status == LessonStatus.CANCELLED

    // Вычисляем длительность перерыва до следующего занятия
    fun getBreakDuration(): String? {
        return if (nextLessonStartTime != null) {
            try {
                val formatter = DateTimeFormatter.ofPattern("H:mm")
                val currentEnd = LocalTime.parse(endTime, formatter)
                val nextStart = LocalTime.parse(nextLessonStartTime, formatter)

                val minutes = Duration.between(currentEnd, nextStart).toMinutes()
                if (minutes > 0) "$minutes минут" else null
            } catch (e: Exception) {
                null
            }
        } else null
    }

    // Время перерыва (диапазон)
    val breakTimeRange: String?
        get() = nextLessonStartTime?.let { "$endTime – $it" }

    // Полное отображение перерыва
    val breakDisplay: String?
        get() {
            val duration = getBreakDuration() ?: return null
            val timeRange = breakTimeRange

            return if (timeRange != null) {
                "перерыв $duration\n$timeRange"
            } else {
                "перерыв $duration"
            }
        }

    // Проверяем, есть ли перерыв после этого занятия
    val hasBreak: Boolean
        get() = !getBreakDuration().isNullOrBlank()
}

enum class LessonType {
    LECTURE,        // лекция
    SEMINAR,        // семинар
    TEST,           // зачет
    EXAM,           // экзамен
    UNKNOWN
}

enum class LessonStatus {
    NORMAL,         // обычное занятие
    CANCELLED,      // отменено
    SUBSTITUTION,   // замена
}