package ru.bogatyreva.class_schedule.domain.model

import androidx.compose.ui.text.input.TextFieldValue
import ru.bogatyreva.class_schedule.utils.PhoneNumberFormatter

data class Student(
    val id: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val avatarUrl: String? = null,
) {
    val fullName: String
        get() = "$lastName $firstName"

    val formattedPhone: String
        get() = formatPhoneNumber(phone)

    private fun formatPhoneNumber(phone: String): String {
        // Извлекаем только цифры
        var digits = PhoneNumberFormatter.extractDigits(phone)

        // Если цифр нет, возвращаем пустую строку
        if (digits.isEmpty()) return ""

        // Заменяем 8 на +7 для российских номеров (если номер начинается с 8 и имеет 11 цифр)
        if (digits.startsWith("8") && digits.length == 11) {
            digits = "7${digits.substring(1)}"
        }

        // Форматируем через PhoneNumberFormatter
        val textFieldValue = PhoneNumberFormatter.format(
            TextFieldValue(digits)
        )
        return textFieldValue.text
    }
}

