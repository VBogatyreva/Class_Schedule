package ru.bogatyreva.class_schedule.domain.usecase

import ru.bogatyreva.class_schedule.domain.repository.ScheduleRepository
import javax.inject.Inject

// Для сдачи задания
class SubmitAssignmentUseCase @Inject constructor (
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(lessonId: Int, fileUri: String): Result<Unit> {
        return repository.submitAssignment(lessonId, fileUri)
    }
}