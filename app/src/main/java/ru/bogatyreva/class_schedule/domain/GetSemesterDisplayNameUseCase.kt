package ru.bogatyreva.class_schedule.domain

//для получения названия семестра ("Весенний семестр 2026 год")

class GetSemesterDisplayNameUseCase (
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(): String {
        return repository.getSemesterDisplayName()
    }
}