package ru.bogatyreva.class_schedule.domain

import java.time.Instant

//для получения дня недели ("Пн", "Вт", "Ср"...)

class GetDayOfWeekUseCase (
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(date: Instant): String {
        return repository.getDayOfWeek(date)
    }
}