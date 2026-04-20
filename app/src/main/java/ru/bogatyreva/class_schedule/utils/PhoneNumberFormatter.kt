package ru.bogatyreva.class_schedule.utils

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import kotlin.math.min


const val PHONE_MASK = "+# (###) ###-##-##" // маска для номера телефона
const val MASK_CHAR = '#'

object PhoneNumberFormatter {

    fun format(textFieldValue: TextFieldValue): TextFieldValue {
        val input = textFieldValue.text
        val digits = input.filter { it.isDigit() }.take(11)

        if (digits.isEmpty() && input.isEmpty()) {
            return TextFieldValue(
                text = "",
                selection = TextRange(0)
            )
        }

        val formatted = applyMask(digits, PHONE_MASK)

        return TextFieldValue(
            text = if (input.first() == '+' && input.length < 2) "+" else formatted,
            selection = if (input.first() == '+' && input.length < 2) TextRange(input.length)
            else TextRange(formatted.length)
        )
    }


    private fun applyMask(number: String, mask: String): String {
        val maskPlaceholderCharCount = mask.count { it == MASK_CHAR }
        var maskCurrentCharIndex = 0
        val output = StringBuilder()

        number.take(min(maskPlaceholderCharCount, number.length)).forEach { digit ->
            for (i in maskCurrentCharIndex until mask.length) {
                if (mask[i] == MASK_CHAR) {
                    output.append(digit)
                    maskCurrentCharIndex += 1
                    break
                } else {
                    output.append(mask[i])
                    maskCurrentCharIndex = i + 1
                }
            }
        }
        return output.toString()
    }

    fun extractDigits(formatted: String): String {
        return formatted.filter { it.isDigit() }
    }
}