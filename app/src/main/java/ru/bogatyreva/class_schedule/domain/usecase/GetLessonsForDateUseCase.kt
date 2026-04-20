package ru.bogatyreva.class_schedule.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.bogatyreva.class_schedule.domain.model.Lesson
import ru.bogatyreva.class_schedule.domain.repository.ScheduleRepository
import java.time.Instant
import javax.inject.Inject

// для получения списка уроков на выбранную дату
class GetLessonsForDateUseCase @Inject constructor (
    private val repository: ScheduleRepository
) {
    operator fun invoke(date: Instant): Flow<List<Lesson>> {
        return repository.getLessonsForDate(date)
    }
}