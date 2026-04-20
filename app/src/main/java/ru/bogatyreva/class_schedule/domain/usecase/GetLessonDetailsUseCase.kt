package ru.bogatyreva.class_schedule.domain.usecase

import ru.bogatyreva.class_schedule.domain.model.LessonDetails
import ru.bogatyreva.class_schedule.domain.repository.ScheduleRepository
import javax.inject.Inject

// Для получения детальной информации о занятии по его ID
class GetLessonDetailsUseCase @Inject constructor (
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(lessonId: Int): LessonDetails {
        return repository.getLessonDetails(lessonId)
    }
}