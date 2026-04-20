package ru.bogatyreva.class_schedule.utils

import androidx.compose.ui.text.input.TextFieldValue

object PhoneNumberFormatter {

    fun format(textFieldValue: TextFieldValue): TextFieldValue {
        val input = textFieldValue.text

        // Извлекаем только цифры
        val digits = input.filter { it.isDigit() }.take(11)

        // Если нет цифр, возвращаем пустую строку
        if (digits.isEmpty()) {
            return TextFieldValue(
                text = "",
                selection = androidx.compose.ui.text.TextRange(0)
            )
        }

        // Форматируем номер
        val formatted = formatDigits(digits)

        // Курсор всегда в конце
        return TextFieldValue(
            text = formatted,
            selection = androidx.compose.ui.text.TextRange(formatted.length)
        )
    }

    private fun formatDigits(digits: String): String {
        val result = StringBuilder()

        when (digits.length) {
            1 -> result.append("+${digits[0]}")
            2 -> result.append("+${digits[0]} (${digits[1]}")
            3 -> result.append("+${digits[0]} (${digits[1]}${digits[2]}")
            4 -> result.append("+${digits[0]} (${digits[1]}${digits[2]}${digits[3]}")
            5 -> result.append("+${digits[0]} (${digits[1]}${digits[2]}${digits[3]}) ${digits[4]}")
            6 -> result.append("+${digits[0]} (${digits[1]}${digits[2]}${digits[3]}) ${digits[4]}${digits[5]}")
            7 -> result.append("+${digits[0]} (${digits[1]}${digits[2]}${digits[3]}) ${digits[4]}${digits[5]}${digits[6]}")
            8 -> result.append("+${digits[0]} (${digits[1]}${digits[2]}${digits[3]}) ${digits[4]}${digits[5]}${digits[6]}-${digits[7]}")
            9 -> result.append("+${digits[0]} (${digits[1]}${digits[2]}${digits[3]}) ${digits[4]}${digits[5]}${digits[6]}-${digits[7]}${digits[8]}")
            10 -> result.append("+${digits[0]} (${digits[1]}${digits[2]}${digits[3]}) ${digits[4]}${digits[5]}${digits[6]}-${digits[7]}${digits[8]}-${digits[9]}")
            11 -> result.append("+${digits[0]} (${digits[1]}${digits[2]}${digits[3]}) ${digits[4]}${digits[5]}${digits[6]}-${digits[7]}${digits[8]}-${digits[9]}${digits[10]}")
            else -> result.append("+${digits[0]} (${digits[1]}${digits[2]}${digits[3]}) ${digits[4]}${digits[5]}${digits[6]}-${digits[7]}${digits[8]}-${digits[9]}${digits[10]}")
        }

        return result.toString()
    }

    fun extractDigits(formatted: String): String {
        return formatted.filter { it.isDigit() }
    }
}