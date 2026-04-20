package ru.bogatyreva.class_schedule.domain.usecase

import ru.bogatyreva.class_schedule.domain.repository.ScheduleRepository
import java.time.Instant
import javax.inject.Inject

//для получения списка дат для горизонтального календаря
class GetCalendarDatesUseCase @Inject constructor (
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(centerDate: Instant): List<Instant> {
        return repository.getCalendarDates(centerDate)
    }
}