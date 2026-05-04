package ru.bogatyreva.class_schedule.data

import ru.bogatyreva.class_schedule.domain.model.Student
import ru.bogatyreva.class_schedule.domain.repository.AuthRepository
import ru.bogatyreva.class_schedule.domain.repository.ProfileRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TestProfileRepositoryImpl @Inject constructor(
    private val authRepository: AuthRepository
) : ProfileRepository {

    override suspend fun getCurrentStudent(): Student? {
        // Получаем текущего студента из AuthRepository
        return authRepository.getCurrentStudent()
    }
}
