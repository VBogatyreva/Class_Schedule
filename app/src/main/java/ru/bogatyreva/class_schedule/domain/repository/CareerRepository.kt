package ru.bogatyreva.class_schedule.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.bogatyreva.class_schedule.domain.model.Vacancy

interface CareerRepository {
    fun getVacancies(): Flow<List<Vacancy>>
    suspend fun getVacancyById(id: String): Vacancy?
}