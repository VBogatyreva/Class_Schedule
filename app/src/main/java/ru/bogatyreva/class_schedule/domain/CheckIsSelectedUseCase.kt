package ru.bogatyreva.class_schedule.domain

import java.time.Instant

//для проверки, является ли дата выбранной

class CheckIsSelectedUseCase (
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(date: Instant, selectedDate: Instant): Boolean {
        return repository.isSelected(date, selectedDate)
    }
}

















