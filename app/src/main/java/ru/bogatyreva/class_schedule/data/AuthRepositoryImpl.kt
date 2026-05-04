package ru.bogatyreva.class_schedule.data

import ru.bogatyreva.class_schedule.data.utils.SessionManager
import ru.bogatyreva.class_schedule.domain.model.AuthResult
import ru.bogatyreva.class_schedule.domain.model.LoginCredentials
import ru.bogatyreva.class_schedule.domain.model.Student
import ru.bogatyreva.class_schedule.domain.repository.AuthRepository
import ru.bogatyreva.class_schedule.utils.PhoneNumberFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val sessionManager: SessionManager
) : AuthRepository {

    private val validCredentials = mapOf(
        "79999999999" to "666666",
        "78888888888" to "111111"
    )

    // Данные студентов
    private val studentsDatabase = mapOf(
        "79999999999" to Student(
            id = "1",
            firstName = "Иван",
            lastName = "Иванов",
            phone = "89999999999",
            avatarUrl = null
        ),
        "78888888888" to Student(
            id = "2",
            firstName = "Петр",
            lastName = "Петров",
            phone = "78888888888",
            avatarUrl = null
        ),
    )

    private var currentStudent: Student? = null
    private var currentToken: String? = null

    init {
        // При создании репозитория восстанавливаем сессию
        restoreSession()
    }

    private fun restoreSession() {
        val token = sessionManager.getAccessToken()
        if (token.isNotEmpty()) {
            currentToken = token
            // Восстанавливаем студента из сохранённых данных
            val userId = sessionManager.getUserId()
            val student = studentsDatabase.values.find { it.id == userId }
            currentStudent = student
        }
    }


    override suspend fun login(credentials: LoginCredentials): AuthResult {
        val rawDigits = PhoneNumberFormatter.extractDigits(credentials.phoneNumber)

        val normalizedPhone = if (rawDigits.startsWith("8") && rawDigits.length == 11) {
            "7${rawDigits.substring(1)}"
        } else {
            rawDigits
        }

        if (validCredentials[normalizedPhone] == credentials.password) {
            currentStudent = studentsDatabase[normalizedPhone]
            currentToken = "mock_token_${System.currentTimeMillis()}"

            // Сохраняем сессию
            currentStudent?.let { student ->
                sessionManager.saveUserId(student.id)
                sessionManager.updateAccessAndRefreshToken(currentToken!!)
                sessionManager.saveCredentials(normalizedPhone, credentials.password)
            }

            return AuthResult.Success(currentToken!!)
        }

        return AuthResult.Error("Неверный номер телефона или пароль")
    }

    override suspend fun logout(): Result<Unit> {
        currentStudent = null
        currentToken = null
        sessionManager.clearTokens()
        return Result.success(Unit)
    }

    override suspend fun getCurrentStudent(): Student? {
        return currentStudent
    }

    override suspend fun isAuthenticated(): Boolean {
        return currentToken != null && sessionManager.getAccessToken().isNotEmpty()
    }
}
