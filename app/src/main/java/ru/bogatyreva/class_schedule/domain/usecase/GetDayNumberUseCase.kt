package ru.bogatyreva.class_schedule.domain.usecase

import ru.bogatyreva.class_schedule.domain.repository.ScheduleRepository
import java.time.Instant
import javax.inject.Inject

//для получения числа месяца ("16", "17", "18"...)
class GetDayNumberUseCase @Inject constructor (
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(date: Instant): String {
        return repository.getDayNumber(date)
    }
}