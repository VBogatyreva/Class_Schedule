package ru.bogatyreva.class_schedule.domain

import java.time.Instant

//для получения числа месяца ("16", "17", "18"...)

class GetDayNumberUseCase (
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(date: Instant): String {
        return repository.getDayNumber(date)
    }
}