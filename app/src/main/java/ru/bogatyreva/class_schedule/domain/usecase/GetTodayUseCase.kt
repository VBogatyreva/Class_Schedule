package ru.bogatyreva.class_schedule.domain.usecase

import ru.bogatyreva.class_schedule.domain.repository.ScheduleRepository
import java.time.Instant
import javax.inject.Inject

//для получения сегодняшней даты
class GetTodayUseCase @Inject constructor (
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(): Instant {
        return repository.getToday()
    }
}