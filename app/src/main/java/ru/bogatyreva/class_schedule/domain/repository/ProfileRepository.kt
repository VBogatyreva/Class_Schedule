package ru.bogatyreva.class_schedule.domain.repository

import ru.bogatyreva.class_schedule.domain.model.Student

interface ProfileRepository {
    suspend fun getCurrentStudent(): Student?
}
