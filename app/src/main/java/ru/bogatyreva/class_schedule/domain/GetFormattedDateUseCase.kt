package ru.bogatyreva.class_schedule.domain

import java.time.Instant

//для получения отформатированной даты для шапки ("25 марта, вторник")

class GetFormattedDateUseCase (
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(date: Instant): String {
        return repository.getFormattedDate(date)
    }
}