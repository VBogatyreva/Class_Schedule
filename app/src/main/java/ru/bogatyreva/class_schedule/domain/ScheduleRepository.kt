package ru.bogatyreva.class_schedule.domain

import kotlinx.coroutines.flow.Flow
import java.time.Instant

interface ScheduleRepository {

    fun getLessonsForDate(date: Instant): Flow<List<Lesson>>          // Основной метод - получение списка уроков
    suspend fun getMonthYear(date: Instant): String                   // Для шапки - "Апрель 2026"
    suspend fun getLessonsCountForDate(date: Instant): Int            // Для шапки - количество пар
    suspend fun getLastLessonEndTime(date: Instant): String?          // Получение времени окончания уроков "16:30"
    suspend fun getCalendarDates(centerDate: Instant): List<Instant>  // Для календаря - какие даты показывать
    suspend fun getDayNumber(date: Instant): String                   // Для календаря - числа (16, 17, 18...)
    suspend fun getDayOfWeek(date: Instant): String                   // Для календаря - дни недели (Пн, Вт...)
    suspend fun getToday(): Instant                                   // Для навигации - кнопка "Сегодня"
    suspend fun isToday(date: Instant): Boolean                        // Для календаря - подсветка сегодня
    suspend fun isSelected(date: Instant, selectedDate: Instant): Boolean   // Для календаря - подсветка выбранного

}