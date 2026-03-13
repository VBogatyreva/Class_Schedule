package ru.bogatyreva.class_schedule.domain

import java.time.Instant

//для получения списка дат для горизонтального календаря

class GetCalendarDatesUseCase (
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(centerDate: Instant): List<Instant> {
        return repository.getCalendarDates(centerDate)
    }
}