package ru.bogatyreva.class_schedule.domain.usecase

import ru.bogatyreva.class_schedule.domain.repository.ScheduleRepository
import java.time.Instant
import javax.inject.Inject

//для проверки, является ли дата выбранной
class CheckIsSelectedUseCase @Inject constructor (
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(date: Instant, selectedDate: Instant): Boolean {
        return repository.isSelected(date, selectedDate)
    }
}