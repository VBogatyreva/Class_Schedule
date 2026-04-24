package ru.bogatyreva.class_schedule.domain.model

data class VacancyDetails(

    val id: String,                           // ID вакансии
    val title: String,                        // Название вакансии
    val incomeLevel: String,                  // Уровень дохода
    val companyName: String,                  // Название компании
    val companyIcon: String,                  // Иконка компании (URL)
    val companyLocation: String,              // Локация компании
    val companyDescription: String,           // Описание компании
    val workFormat: String,                   // Формат работы (удаленно/офис)
    val employment: String,                   // Тип занятости
    val applyingJob: String,                  // Оформление
    val schedule: String,                     // График работы
    val workingHours: Int,                    // Рабочие часы в день
    val frequencyOfPayments: String,          // Частота выплат
    val experience: String,                   // Требуемый опыт
    val requiredEducation: String,            // Требуемое образование
    val recruitmentRequirements: String,      // Требования к соискателю
    val responsibilities: String,             // Обязанности
    val conditions: String,                   // Условия работы
    val keySkills: List<String>,              // Ключевые навыки
    val address: String                       // Адрес места работы
)
