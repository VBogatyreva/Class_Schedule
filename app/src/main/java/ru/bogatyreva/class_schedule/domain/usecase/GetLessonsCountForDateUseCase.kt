package ru.bogatyreva.class_schedule.domain.usecase

import ru.bogatyreva.class_schedule.domain.repository.ScheduleRepository
import java.time.Instant

//для получения количества пар на выбранную дату
class GetLessonsCountForDateUseCase (
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(date: Instant): Int {
        return repository.getLessonsCountForDate(date)
    }
}