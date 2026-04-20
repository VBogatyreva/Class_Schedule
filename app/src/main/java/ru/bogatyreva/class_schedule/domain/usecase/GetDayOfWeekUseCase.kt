package ru.bogatyreva.class_schedule.domain.usecase

import ru.bogatyreva.class_schedule.domain.repository.ScheduleRepository
import java.time.Instant
import javax.inject.Inject

//для получения дня недели ("Пн", "Вт", "Ср"...)
class GetDayOfWeekUseCase @Inject constructor (
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(date: Instant): String {
        return repository.getDayOfWeek(date)
    }
}