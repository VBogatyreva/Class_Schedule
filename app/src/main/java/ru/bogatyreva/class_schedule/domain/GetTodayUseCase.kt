package ru.bogatyreva.class_schedule.domain

import java.time.Instant

//для получения сегодняшней даты

class GetTodayUseCase (
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(): Instant {
        return repository.getToday()
    }
}