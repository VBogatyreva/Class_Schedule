package ru.bogatyreva.class_schedule.domain.model

import androidx.compose.ui.text.input.TextFieldValue

data class LoginCredentials(
    val phoneNumber: String,    // Номер телефона, который ввел пользователь
    val password: String        // Пароль, который ввел пользователь
)

data class AuthState(
    val isAuthenticated: Boolean = false,                         // выполнил ли пользователь успешный вход в систему
    val isLoading: Boolean = false,                               // индикатор выполнения условий входа
    val error: String? = null,                                    // хранение сообщения об ошибке при неудачной попытке входа
    val phoneNumber: TextFieldValue = TextFieldValue(""),  // хранение и управление введённым номером телефона с сохранением позиции курсора
    val password: String = "",                                   // хранение введённого пароля в виде обычной строки
    val showPassword: Boolean = false                            // управление видимостью пароля в поле ввода
) {
    val isLoginEnabled: Boolean
        get() {
            // извлекаем только цифры из номера телефона "+7 (999) 123-45-67" → "79991234567"
            val digits = phoneNumber.text.filter { it.isDigit() }

            // проверяем три условия:
            return digits.length == 11 &&    // в номере ровно 11 цифр
                    password.length >= 6 &&   // пароль содержит минимум 6 символов
                    !isLoading                // не идет процесс загрузки/отправки
        }
}

sealed class AuthResult {
    data class Success(val userToken: String) : AuthResult()  // Вход успешен
    data class Error(val message: String) : AuthResult()      // Вход не удался
}
