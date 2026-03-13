package ru.bogatyreva.class_schedule.domain

import java.time.Instant

//для получения отформатированной даты для шапки ("16 марта, понедельник")

class GetFormattedDateUseCase (
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(date: Instant): String {
        return repository.getFormattedDate(date)
    }
}