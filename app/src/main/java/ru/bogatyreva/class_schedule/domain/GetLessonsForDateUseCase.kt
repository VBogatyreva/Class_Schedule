package ru.bogatyreva.class_schedule.domain

import kotlinx.coroutines.flow.Flow
import java.time.Instant

// для получения списка уроков на выбранную дату

class GetLessonsForDateUseCase (
    private val repository: ScheduleRepository
) {
    operator fun invoke(date: Instant): Flow<List<Lesson>> {
        return repository.getLessonsForDate(date)
    }
}