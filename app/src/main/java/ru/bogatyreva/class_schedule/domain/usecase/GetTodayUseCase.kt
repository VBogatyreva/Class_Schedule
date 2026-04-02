package ru.bogatyreva.class_schedule.domain.usecase

import ru.bogatyreva.class_schedule.domain.repository.ScheduleRepository
import java.time.Instant

//для получения сегодняшней даты
class GetTodayUseCase (
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(): Instant {
        return repository.getToday()
    }
}