package ru.bogatyreva.class_schedule.domain.usecase

import ru.bogatyreva.class_schedule.domain.repository.ScheduleRepository

// Для сдачи задания
class SubmitAssignmentUseCase(
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(lessonId: Int, fileUri: String): Result<Unit> {
        return repository.submitAssignment(lessonId, fileUri)
    }
}