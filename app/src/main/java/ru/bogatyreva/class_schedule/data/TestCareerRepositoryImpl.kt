package ru.bogatyreva.class_schedule.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ru.bogatyreva.class_schedule.domain.model.Vacancy
import ru.bogatyreva.class_schedule.domain.repository.CareerRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TestCareerRepositoryImpl @Inject constructor() : CareerRepository {

//    private val mockVacancies = emptyList<Vacancy>()   // для случая, когда вакансий нет

    private val mockVacancies = listOf(
        Vacancy(
            id = "1",
            title = "Middle UX/UI-дизайнер",
            companyName = "Федеральная мониторинговая компания",
            salaryMin = 120_000,
            salaryMax = 240_000,
            employmentType = "проект",
            experienceMin = 1,
            experienceMax = 3,
            isRemote = true,
            isOnSite = true,
            tags = listOf("UX/UI", "Дизайн"),
            imageUrl = "https://example.com/photo.jpg"
        ),
        Vacancy(
            id = "2",
            title = "Senior Android Developer",
            companyName = "ТехноСофт",
            salaryMin = 250_000,
            salaryMax = 350_000,
            employmentType = "полная занятость",
            experienceMin = 3,
            experienceMax = 5,
            isRemote = true,
            isOnSite = false,
            tags = listOf("Android", "Kotlin"),
            imageUrl = null  // Нет изображения
        ),
        Vacancy(
            id = "3",
            title = "Product Manager",
            companyName = "Стартап Инновации",
            salaryMin = null,  // Нет зарплаты
            salaryMax = null,
            employmentType = "проект",
            experienceMin = 2,
            experienceMax = 4,
            isRemote = false,
            isOnSite = true,
            tags = listOf("Product", "Agile")
        ),
        Vacancy(
            id = "4",
            title = "Junior Frontend Developer",
            companyName = "ВебСтудия",
            salaryMin = 80_000,
            salaryMax = null,
            employmentType = "полная занятость",
            experienceMin = 1,
            experienceMax = 2,
            isRemote = true,
            isOnSite = true,
            tags = listOf("React", "TypeScript")
        ),
        Vacancy(
            id = "5",
            title = "Юрист по международному праву (стажер)",
            companyName = "Конкорд Лигал",
            salaryMin = 55_000,
            salaryMax = 80_000,
            employmentType = "стажировка",
            experienceMin = 0,
            experienceMax = 1,
            isRemote = false,
            isOnSite = true,
            tags = listOf("Международное право", "Английский язык", "Договоры"),
            imageUrl = null
        ),
        Vacancy(
            id = "6",
            title = "Помощник судьи (арбитраж)",
            companyName = "Арбитражный суд г. Москвы",
            salaryMin = 65_000,
            salaryMax = 85_000,
            employmentType = "полная занятость",
            experienceMin = 0,
            experienceMax = 2,
            isRemote = false,
            isOnSite = true,
            tags = listOf("Арбитражный процесс", "АПК РФ", "Судебная практика"),
            imageUrl = null
        ),
        Vacancy(
            id = "7",
            title = "Юрист по трудовому праву (junior)",
            companyName = "HR Legal",
            salaryMin = 60_000,
            salaryMax = 90_000,
            employmentType = "полная занятость",
            experienceMin = 0,
            experienceMax = 2,
            isRemote = true,
            isOnSite = false,
            tags = listOf("Трудовое право", "Кадровое делопроизводство", "ТК РФ"),
            imageUrl = null
        ),
        Vacancy(
            id = "8",
            title = "Специалист по банкротству (стажер)",
            companyName = "Банкротство.Консалт",
            salaryMin = 50_000,
            salaryMax = 70_000,
            employmentType = "стажировка",
            experienceMin = 0,
            experienceMax = 1,
            isRemote = false,
            isOnSite = true,
            tags = listOf("Банкротство", "127-ФЗ", "Реестр кредиторов"),
            imageUrl = null
        ),
        Vacancy(
            id = "9",
            title = "Помощник юриста (гражданское право)",
            companyName = "Юридический центр «Правовед»",
            salaryMin = 55_000,
            salaryMax = 75_000,
            employmentType = "полная занятость",
            experienceMin = 0,
            experienceMax = 1,
            isRemote = false,
            isOnSite = true,
            tags = listOf("Гражданское право", "Иски", "Претензионная работа"),
            imageUrl = null
        ),
        Vacancy(
            id = "10",
            title = "Младший юрист (комплаенс)",
            companyName = "Сбербанк",
            salaryMin = 90_000,
            salaryMax = 130_000,
            employmentType = "полная занятость",
            experienceMin = 0,
            experienceMax = 2,
            isRemote = false,
            isOnSite = true,
            tags = listOf("Комплаенс", "115-ФЗ", "Антикоррупция"),
            imageUrl = null
        )
    )

    override fun getVacancies(): Flow<List<Vacancy>> = flowOf(mockVacancies)

    override suspend fun getVacancyById(id: String): Vacancy? {
        return mockVacancies.find { it.id == id }
    }
}