package ru.bogatyreva.class_schedule.domain.model

data class Vacancy(
    val id: String,
    val title: String,                    // Название вакансии
    val companyName: String,              // Название компании/проекта
    val salaryMin: Int? = null,           // Нижняя граница зарплаты
    val salaryMax: Int? = null,           // Верхняя граница зарплаты
    val employmentType: String,           // "проект", "полная занятость" и т.д.
    val experienceMin: Int,               // Минимальный опыт (лет)
    val experienceMax: Int,               // Максимальный опыт (лет)
    val isRemote: Boolean,                // Удаленно или нет
    val isOnSite: Boolean,                // На месте работодателя или нет
    val imageUrl: String? = null,         // Ссылка на изображение вакансии
    val tags: List<String> = emptyList()  // Дополнительные теги
) {
    // Форматированная строка зарплаты (без "на руки")
    val salaryDisplay: String
        get() = when {
            salaryMin != null && salaryMax != null -> {
                val minFormatted = formatSalary(salaryMin)
                val maxFormatted = formatSalary(salaryMax)
                "от $minFormatted до $maxFormatted ₽"
            }
            salaryMin != null -> {
                val minFormatted = formatSalary(salaryMin)
                "от $minFormatted ₽"
            }
            salaryMax != null -> {
                val maxFormatted = formatSalary(salaryMax)
                "до $maxFormatted ₽"
            }
            else -> "Уровень дохода не указан"
        }

    // Форматированная строка опыта с правильным склонением
    val experienceDisplay: String
        get() = when {
            experienceMin == experienceMax -> formatYearsWithDeclension(experienceMin)
            else -> "от $experienceMin до $experienceMax лет"
        }

    // Форматирование числа с пробелами (120000 → 120 000)
    private fun formatSalary(amount: Int): String {
        return String.format("%,d", amount).replace(',', ' ')
    }

    // Склонение слова "год/года/лет"
    private fun formatYearsWithDeclension(years: Int): String {
        return when {
            years % 10 == 1 && years % 100 != 11 -> "$years год"
            years % 10 in 2..4 && (years % 100 !in 12..14) -> "$years года"
            else -> "$years лет"
        }
    }
}