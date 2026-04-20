package ru.bogatyreva.class_schedule.domain.usecase

import ru.bogatyreva.class_schedule.domain.repository.AuthRepository
import javax.inject.Inject

// для выполнения выхода из аккаунта
class LogoutUseCase @Inject constructor (
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return authRepository.logout()
    }
}