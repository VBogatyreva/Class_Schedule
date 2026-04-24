package ru.bogatyreva.class_schedule.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.bogatyreva.class_schedule.domain.model.Vacancy
import ru.bogatyreva.class_schedule.domain.model.VacancyDetails

interface CareerRepository {
    fun getVacancies(): Flow<List<Vacancy>>
    suspend fun getVacancyDetailsById(id: String): VacancyDetails?
}