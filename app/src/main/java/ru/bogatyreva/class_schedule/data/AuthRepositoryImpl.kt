package ru.bogatyreva.class_schedule.data

import ru.bogatyreva.class_schedule.domain.model.AuthResult
import ru.bogatyreva.class_schedule.domain.model.LoginCredentials
import ru.bogatyreva.class_schedule.domain.repository.AuthRepository

class AuthRepositoryImpl : AuthRepository {

    private val validCredentials = mapOf(
        "79999999999" to "666666",
        "78888888888" to "111111"
    )

    override suspend fun login(credentials: LoginCredentials): AuthResult {
        // credentials.phoneNumber уже содержит только цифры
        val phoneDigits = credentials.phoneNumber

        return if (validCredentials[phoneDigits] == credentials.password) {
            AuthResult.Success("mock_token_${System.currentTimeMillis()}")
        } else {
            AuthResult.Error("Неверный номер телефона или пароль")
        }
    }

    override suspend fun logout(): Result<Unit> {
        return Result.success(Unit)
    }
}