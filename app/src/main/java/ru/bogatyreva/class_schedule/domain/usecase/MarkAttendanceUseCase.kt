package ru.bogatyreva.class_schedule.domain.usecase

import ru.bogatyreva.class_schedule.domain.model.AttendanceStatus
import ru.bogatyreva.class_schedule.domain.repository.ScheduleRepository
import javax.inject.Inject

// Для отметки посещаемости занятия
class MarkAttendanceUseCase @Inject constructor (
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(lessonId: Int, status: AttendanceStatus): Result<Unit> {
        return repository.markAttendance(lessonId, status)
    }
}