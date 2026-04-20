package ru.bogatyreva.class_schedule.domain.usecase

import ru.bogatyreva.class_schedule.domain.repository.ScheduleRepository
import java.time.Instant
import javax.inject.Inject

//для проверки, является ли дата сегодняшней
class CheckIsTodayUseCase @Inject constructor (
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(date: Instant): Boolean {
        return repository.isToday(date)
    }
}