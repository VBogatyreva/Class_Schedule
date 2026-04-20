package ru.bogatyreva.class_schedule.domain.usecase

import ru.bogatyreva.class_schedule.domain.repository.ScheduleRepository
import java.time.Instant
import javax.inject.Inject

//для получения количества пар на выбранную дату
class GetLessonsCountForDateUseCase @Inject constructor (
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(date: Instant): Int {
        return repository.getLessonsCountForDate(date)
    }
}