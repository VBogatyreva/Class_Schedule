package ru.bogatyreva.class_schedule.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.bogatyreva.class_schedule.domain.model.Vacancy
import ru.bogatyreva.class_schedule.domain.repository.CareerRepository
import javax.inject.Inject

// для получения списка всех доступных вакансий и отображения на экране "Карьера"
class GetVacanciesUseCase @Inject constructor(
    private val repository: CareerRepository
) {
    operator fun invoke(): Flow<List<Vacancy>> = repository.getVacancies()
}