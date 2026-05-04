package ru.bogatyreva.class_schedule.domain.repository

import ru.bogatyreva.class_schedule.domain.model.AuthResult
import ru.bogatyreva.class_schedule.domain.model.LoginCredentials
import ru.bogatyreva.class_schedule.domain.model.Student

interface AuthRepository {
    suspend fun login(credentials: LoginCredentials): AuthResult
    suspend fun logout(): Result<Unit>
    suspend fun getCurrentStudent(): Student?
    suspend fun isAuthenticated(): Boolean

}