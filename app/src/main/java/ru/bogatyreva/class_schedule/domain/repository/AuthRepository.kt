package ru.bogatyreva.class_schedule.domain.repository

import ru.bogatyreva.class_schedule.domain.model.AuthResult
import ru.bogatyreva.class_schedule.domain.model.LoginCredentials

interface AuthRepository {
    suspend fun login(credentials: LoginCredentials): AuthResult
    suspend fun logout(): Result<Unit>
}