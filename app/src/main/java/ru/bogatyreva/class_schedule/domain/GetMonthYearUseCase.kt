package ru.bogatyreva.class_schedule.domain

import java.time.Instant

class GetMonthYearUseCase (
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(date: Instant): String {
        return repository.getMonthYear(date)
    }
}