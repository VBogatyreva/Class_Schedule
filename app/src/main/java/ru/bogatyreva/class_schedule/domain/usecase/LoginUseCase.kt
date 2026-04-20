package ru.bogatyreva.class_schedule.domain.usecase

import ru.bogatyreva.class_schedule.domain.model.AuthResult
import ru.bogatyreva.class_schedule.domain.model.LoginCredentials
import ru.bogatyreva.class_schedule.domain.repository.AuthRepository

// для выполнения входа в приложение
class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(phoneNumber: String, password: String): AuthResult {
        val credentials = LoginCredentials(
            phoneNumber = phoneNumber,
            password = password
        )
        return authRepository.login(credentials)
    }
}