package ru.bogatyreva.class_schedule.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import ru.bogatyreva.class_schedule.domain.model.Assignment
import ru.bogatyreva.class_schedule.domain.model.AttendanceStatus
import ru.bogatyreva.class_schedule.domain.model.Lesson
import ru.bogatyreva.class_schedule.domain.model.LessonDetails
import ru.bogatyreva.class_schedule.domain.model.LessonMaterial
import ru.bogatyreva.class_schedule.domain.model.LessonStatus
import ru.bogatyreva.class_schedule.domain.model.LessonType
import ru.bogatyreva.class_schedule.domain.model.SubmittedFile
import ru.bogatyreva.class_schedule.domain.model.SubmittedMaterial
import ru.bogatyreva.class_schedule.domain.repository.ScheduleRepository
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

class TestScheduleRepositoryImpl @Inject constructor(
) : ScheduleRepository {

    // Фиксирую дату для тестовых данных на: Понедельник, 30 марта 2026 года
    private val fixedDate = LocalDate.of(2026, 3, 30)
        .atStartOfDay(ZoneId.systemDefault()) // Начало дня (00:00)
        .toInstant()

    // Расписание на неделю:
    private val mockLessons = listOf(

        // 30 марта (Понедельник)
        // Урок 1: СДАНО, есть файлы, имя полное, материалы всех типов
        Lesson(
            id = 1,
            pairNumber = 1,
            startTime = "09:00",
            endTime = "10:30",
            discipline = "Государственное регулирование инновационной деятельности",
            audience = "101",
            originalTeacher = "Сергеев Антон Викторович",
            date = getDateForDayOffset(0),
            lessonType = LessonType.LECTURE,
            status = LessonStatus.NORMAL,
            lessonMaterials = listOf(
                // PDF файл
                LessonMaterial(
                    id = "1",
                    fileName = "Лекция_Государственное_регулирование.pdf",
                    fileSize = "3.2 MB",
                    fileUrl = "https://example.com/lecture.pdf",
                    fileType = "PDF"
                ),
                // PPTX презентация
                LessonMaterial(
                    id = "2",
                    fileName = "Презентация_к_лекции.pptx",
                    fileSize = "2.1 MB",
                    fileUrl = "https://example.com/presentation.pptx",
                    fileType = "PPTX"
                ),
                // DOCX документ
                LessonMaterial(
                    id = "3",
                    fileName = "Дополнительные_материалы.docx",
                    fileSize = "1.5 MB",
                    fileUrl = "https://example.com/materials.docx",
                    fileType = "DOCX"
                ),
                // JPG изображение
                LessonMaterial(
                    id = "4",
                    fileName = "Схема_регулирования.jpg",
                    fileSize = "0.8 MB",
                    fileUrl = "https://example.com/schema.jpg",
                    fileType = "JPG"
                ),
                // Ссылка (LINK)
                LessonMaterial(
                    id = "5",
                    fileName = "Полезная_ссылка",
                    fileSize = "",
                    fileUrl = "https://example.com/useful-link",
                    fileType = "LINK"
                )
            ),
            assignment = Assignment(
                id = "1",
                title = "Задание по истории государства",
                description = "Напишите эссе на тему 'Развитие государства в России' объемом 2-3 страницы.",
                deadline = "30 марта 2026, 23:59",
                submitted = true,
                submittedFiles = listOf(
                    SubmittedFile(
                        id = "file_1_1",
                        fileName = "эссе_история.pdf",
                        fileSize = "1.2 MB",
                        fileType = "PDF"
                    )
                )
            )
        ),
        // Урок 2: СДАНО, есть файлы, имя  неполное, материалы отсутствуют
        Lesson(
            id = 2,
            pairNumber = 2,
            startTime = "10:45",
            endTime = "12:15",
            discipline = "Спортивное право",
            audience = "501",
            originalTeacher = "Иванов Петр",
            date = getDateForDayOffset(0),
            lessonType = LessonType.SEMINAR,
            status = LessonStatus.NORMAL,
            lessonMaterials = emptyList(),  // ← материалы отсутствуют
            assignment = Assignment(
                id = "2",
                title = "Задание по спортивному праву",
                description = "Подготовьте презентацию на тему 'Правовое регулирование спорта в РФ'.",
                deadline = "2 апреля 2026, 23:59",
                submitted = true,
                submittedFiles = listOf(
                    SubmittedFile(
                        id = "file_2_1",
                        fileName = "Спортивное_право_презентация.pptx",
                        fileSize = "2.1 MB",
                        fileType = "PPTX"
                    )
                )
            )
        ),

        // 31 марта (Вторник)
        // Урок 3: НЕ СДАНО, ЕСТЬ файлы
        Lesson(
            id = 3,
            pairNumber = 1,
            startTime = "10:00",
            endTime = "11:30",
            discipline = "Римское право",
            audience = "203",
            originalTeacher = "Романова Елена Викторовна",
            date = getDateForDayOffset(1),
            lessonType = LessonType.LECTURE,
            status = LessonStatus.NORMAL,
            lessonMaterials = listOf(
                LessonMaterial("1", "Римское_право_доп_материалы.docx", "1.5 MB", "", "DOCX")
            ),
            assignment = Assignment(
                id = "3",
                title = "Римское право",
                description = "Составьте глоссарий основных терминов римского права (не менее 20 терминов).",
                deadline = "1 апреля 2026, 23:59",
                submitted = false,
                submittedFiles = listOf(
                    SubmittedFile(
                        id = "file_3_1",
                        fileName = "глоссарий_римское_право.docx",
                        fileSize = "0.5 MB",
                        fileType = "DOCX"
                    )
                )
            )
        ),
        // Урок 4: НЕ СДАНО, НЕТ файлов
        Lesson(
            id = 4,
            pairNumber = 2,
            startTime = "11:45",
            endTime = "13:15",
            discipline = "Международное право",
            audience = "405",
            originalTeacher = "Иванов Иван Иванович",
            date = getDateForDayOffset(1),
            lessonType = LessonType.SEMINAR,
            status = LessonStatus.SUBSTITUTION,
            substitutionTeacher = "Кузнецов Андрей Владимирович",
            lessonMaterials = listOf(
                LessonMaterial("1", "Международное_право_ссылка", "", "", "LINK")
            ),
            assignment = Assignment(
                id = "4",
                title = "Международное право",
                description = "Сделать всех счастливыми",
                deadline = "25 марта 2026, 23:59",
                submitted = false,
                submittedFiles = emptyList()
            )
        ),

        // 1 апреля (Среда)
        // Урок 5: НЕ СДАНО, НЕТ файлов
        Lesson(
            id = 5,
            pairNumber = 1,
            startTime = "14:00",
            endTime = "15:30",
            discipline = "Государственное регулирование",
            audience = "306",
            originalTeacher = "Петров Петр Петрович",
            date = getDateForDayOffset(2),
            lessonType = LessonType.EXAM,
            status = LessonStatus.NORMAL,
            lessonMaterials = listOf(
                LessonMaterial("1", "Гос_регулирование_замена.pdf", "1.5 MB", "", "PDF")
            ),
            assignment = Assignment(
                id = "19", title = "Государственное регулирование (замена)",
                description = "Проработайте материалы по теме 'Государственное регулирование'.",
                deadline = "9 апреля 2026, 23:59", submitted = false, submittedFiles = emptyList()
            )
        ),

        // 2 апреля (Четверг)
        // Урок 6: СДАНО, есть 2 файла
        Lesson(
            id = 6,
            pairNumber = 1,
            startTime = "09:00",
            endTime = "10:30",
            discipline = "Криминалистика",
            audience = "402",
            originalTeacher = "Ветрова Анна Сергеевна",
            date = getDateForDayOffset(3),
            lessonType = LessonType.LECTURE,
            status = LessonStatus.CANCELLED,
            lessonMaterials = listOf(
                LessonMaterial("1", "Криминалистика_лекция.pdf", "4.1 MB", "", "PDF"),
                LessonMaterial("2", "Дополнительные_материалы.jpg", "0.8 MB", "", "JPG")
            ),
            assignment = Assignment(
                id = "6",
                title = "Криминалистика",
                description = "Подготовьте реферат на тему 'Методы криминалистики' объемом 5-7 страниц.",
                deadline = "5 апреля 2026, 23:59",
                submitted = true,
                submittedFiles = listOf(
                    SubmittedFile(
                        id = "file_6_1",
                        fileName = "Криминалистика_реферат.pdf",
                        fileSize = "1.2 MB",
                        fileType = "PDF"
                    ),
                    SubmittedFile(
                        id = "file_6_2",
                        fileName = "IMG_20250330_схема.jpg",
                        fileSize = "0.5 MB",
                        fileType = "JPG"
                    )
                )
            )
        ),
        // 3 апреля (Пятница)
        // Урок 7: НЕ СДАНО, НЕТ файлов
        Lesson(
            id = 7,
            pairNumber = 1,
            startTime = "13:00",
            endTime = "14:30",
            discipline = "Судебная медицина",
            audience = null,
            originalTeacher = "Волков Сергей Петрович",
            date = getDateForDayOffset(4),
            lessonType = LessonType.LECTURE,
            status = LessonStatus.NORMAL,
            isOnline = true,
            onlineLink = "https://meet.google.com/xxx-xxxx-xxx",
            lessonMaterials = emptyList(),
            assignment = Assignment(
                id = "7",
                title = "Судебная медицина",
                description = "Изучите материалы по теме 'Судебно-медицинская экспертиза'.",
                deadline = "7 апреля 2026, 23:59",
                submitted = false,
                submittedFiles = emptyList()
            )
        ),
        // Суббота (4 апреля) - выходной, уроков нет
        // Воскресенье (5 апреля) - выходной, уроков нет

        // 6 апреля (Понедельник)
        Lesson(
            id = 8,
            pairNumber = 1,
            startTime = "10:00",
            endTime = "11:45",
            discipline = "Международное право",
            audience = "405",
            originalTeacher = "Иванов Иван Иванович",
            date = getDateForDayOffset(7),
            lessonType = LessonType.LECTURE,
            status = LessonStatus.NORMAL,
            lessonMaterials = listOf(
                LessonMaterial("1", "Лекция_Международное_право.pdf", "2.5 MB", "", "PDF"),
                LessonMaterial("2", "Презентация_к_занятию.pptx", "1.8 MB", "", "PPTX"),
                LessonMaterial("3", "Дополнительные_материалы.docx", "0.9 MB", "", "DOCX")
            ),
            assignment = Assignment(
                id = "8",
                title = "Международное право",
                description = "Напишите эссе на тему 'Нормы международного права' объемом 2-3 страницы.",
                deadline = "30 марта 2026, 23:59",
                submitted = false,
                submittedFiles = emptyList()
            )
        ),
        Lesson(
            id = 9,
            pairNumber = 2,
            startTime = "12:00",
            endTime = "13:45",
            discipline = "Государственное регулирование",
            audience = "306",
            originalTeacher = "Петров Петр Петрович",
            date = getDateForDayOffset(7),
            lessonType = LessonType.SEMINAR,
            status = LessonStatus.NORMAL,
            lessonMaterials = listOf(
                LessonMaterial("1", "Гос_регулирование_лекция.pdf", "2.2 MB", "", "PDF")
            ),
            assignment = Assignment(
                id = "9",
                title = "Государственное регулирование",
                description = "Подготовьте доклад на тему 'Государственное регулирование экономики'.",
                deadline = "3 апреля 2026, 23:59",
                submitted = false,
                submittedFiles = emptyList()
            )
        ),
        Lesson(
            id = 10,
            pairNumber = 3,
            startTime = "14:00",
            endTime = "15:45",
            discipline = "Уголовное право",
            audience = "205",
            originalTeacher = "Смирнов Алексей Игоревич",
            date = getDateForDayOffset(7),
            lessonType = LessonType.LECTURE,
            status = LessonStatus.CANCELLED,
            lessonMaterials = emptyList(),
            assignment = Assignment(
                id = "10",
                title = "Уголовное право",
                description = "Подготовьте ответы на вопросы по теме 'Преступление и наказание'.",
                deadline = "9 апреля 2026, 23:59",
                submitted = false,
                submittedFiles = emptyList()
            )
        ),
        Lesson(
            id = 11,
            pairNumber = 4,
            startTime = "16:00",
            endTime = "17:45",
            discipline = "История России",
            audience = "408",
            originalTeacher = "Васильев Игорь Александрович",
            date = getDateForDayOffset(7),
            lessonType = LessonType.EXAM,
            status = LessonStatus.CANCELLED,
            lessonMaterials = emptyList(),
            assignment = Assignment(
                id = "11",
                title = "История России",
                description = "Подготовьте эссе на тему 'Россия в XIX веке'.",
                deadline = "10 апреля 2026, 23:59",
                submitted = false,
                submittedFiles = emptyList()
            )
        ),

        // 7 апреля (Вторник)
        Lesson(
            id = 12,
            pairNumber = 1,
            startTime = "10:00",
            endTime = "11:45",
            discipline = "Гражданское право",
            audience = "301",
            originalTeacher = "Петрова Елена Сергеевна",
            date = getDateForDayOffset(8),
            lessonType = LessonType.EXAM,
            status = LessonStatus.NORMAL,
            lessonMaterials = listOf(
                LessonMaterial("1", "Гражданское_право_материалы.pdf", "3.5 MB", "", "PDF")
            ),
            assignment = Assignment(
                id = "12",
                title = "Гражданское право",
                description = "Решите задачи по гражданскому праву (задачи в приложении).",
                deadline = "4 апреля 2026, 23:59",
                submitted = false,
                submittedFiles = emptyList()
            )
        ),
        Lesson(
            id = 13,
            pairNumber = 2,
            startTime = "12:00",
            endTime = "13:45",
            discipline = "Административное право",
            audience = "302",
            originalTeacher = "Соколов Дмитрий Иванович",
            date = getDateForDayOffset(8),
            lessonType = LessonType.LECTURE,
            status = LessonStatus.NORMAL,
            lessonMaterials = listOf(
                LessonMaterial("1", "Административное_право.pptx", "1.9 MB", "", "PPTX")
            ),
            assignment = Assignment(
                id = "13",
                title = "Административное право",
                description = "Составьте схему 'Система административного права'.",
                deadline = "6 апреля 2026, 23:59",
                submitted = false,
                submittedFiles = emptyList()
            )
        ),
        Lesson(
            id = 14,
            pairNumber = 3,
            startTime = "14:00",
            endTime = "15:45",
            discipline = "Философия",
            audience = "103",
            originalTeacher = "Кравцова Ирина Андреевна",
            date = getDateForDayOffset(8),
            lessonType = LessonType.SEMINAR,
            status = LessonStatus.NORMAL,
            lessonMaterials = emptyList(),
            assignment = Assignment(
                id = "14",
                title = "Философия",
                description = "Напишите реферат на тему 'Античная философия'.",
                deadline = "10 апреля 2026, 23:59",
                submitted = false,
                submittedFiles = emptyList()
            )
        ),

        // 8 апреля (Среда)
        Lesson(
            id = 15,
            pairNumber = 1,
            startTime = "10:00",
            endTime = "11:45",
            discipline = "Экологическое право",
            audience = "201",
            originalTeacher = "Козлова Анна Александровна",
            date = getDateForDayOffset(9),
            lessonType = LessonType.TEST,
            status = LessonStatus.NORMAL,
            lessonMaterials = listOf(
                LessonMaterial("1", "Экологическое_право.pdf", "2.8 MB", "", "PDF")
            ),
            assignment = Assignment(
                id = "15", title = "Экологическое право",
                description = "Напишите эссе на тему 'Экологические проблемы современности'.",
                deadline = "7 апреля 2026, 23:59", submitted = false, submittedFiles = emptyList()
            )
        ),
        Lesson(
            id = 16,
            pairNumber = 2,
            startTime = "12:00",
            endTime = "13:45",
            discipline = "Социология",
            audience = "608",
            originalTeacher = "Орлова Татьяна Васильевна",
            date = getDateForDayOffset(9),
            lessonType = LessonType.LECTURE,
            status = LessonStatus.NORMAL,
            lessonMaterials = emptyList(),
            assignment = null
        ),
        Lesson(
            id = 17,
            pairNumber = 3,
            startTime = "14:00",
            endTime = "15:45",
            discipline = "Трудовое право",
            audience = "202",
            originalTeacher = "Николаев Петр Сергеевич",
            date = getDateForDayOffset(9),
            lessonType = LessonType.SEMINAR,
            status = LessonStatus.NORMAL,
            lessonMaterials = emptyList(),
            assignment = null
        ),

        // 9 апреля (Четверг)
        Lesson(
            id = 18,
            pairNumber = 1,
            startTime = "10:00",
            endTime = "11:45",
            discipline = "Финансовое право",
            audience = "401",
            originalTeacher = "Зайцева Ольга Викторовна",
            date = getDateForDayOffset(10),
            lessonType = LessonType.LECTURE,
            status = LessonStatus.NORMAL,
            lessonMaterials = listOf(
                LessonMaterial("1", "Финансовое_право_лекция.pdf", "3.1 MB", "", "PDF")
            ),
            assignment = Assignment(
                id = "18", title = "Финансовое право",
                description = "Подготовьте анализ налоговой системы РФ.",
                deadline = "8 апреля 2026, 23:59", submitted = false, submittedFiles = emptyList()
            )
        ),
        Lesson(
            id = 19,
            pairNumber = 2,
            startTime = "12:00",
            endTime = "13:45",
            discipline = "Государственное регулирование",
            audience = "306",
            originalTeacher = "Петров Петр Петрович",
            date = getDateForDayOffset(10),
            lessonType = LessonType.SEMINAR,
            status = LessonStatus.SUBSTITUTION,
            substitutionTeacher = "Белов Михаил Павлович",
            lessonMaterials = listOf(
                LessonMaterial("1", "Гос_регулирование_замена.pdf", "1.5 MB", "", "PDF")
            ),
            assignment = Assignment(
                id = "19", title = "Государственное регулирование (замена)",
                description = "Проработайте материалы по теме 'Государственное регулирование'.",
                deadline = "9 апреля 2026, 23:59", submitted = false, submittedFiles = emptyList()
            )
        ),
        Lesson(
            id = 20,
            pairNumber = 3,
            startTime = "14:00",
            endTime = "15:45",
            discipline = "Государственное регулирование",
            audience = "306",
            originalTeacher = "Петров Петр Петрович",
            date = getDateForDayOffset(10),
            lessonType = LessonType.SEMINAR,
            status = LessonStatus.NORMAL,
            lessonMaterials = emptyList(),
            assignment = null
        ),

        // 10 апреля (Пятница)
        Lesson(
            id = 21,
            pairNumber = 1,
            startTime = "10:00",
            endTime = "11:45",
            discipline = "Налоговое право",
            audience = "402",
            originalTeacher = "Павлов Андрей Игоревич",
            date = getDateForDayOffset(11),
            lessonType = LessonType.SEMINAR,
            status = LessonStatus.NORMAL,
            lessonMaterials = emptyList(),
            assignment = null
        ),
        Lesson(
            id = 22,
            pairNumber = 2,
            startTime = "12:00",
            endTime = "13:45",
            discipline = "Административное право",
            audience = "302",
            originalTeacher = "Соколов Дмитрий Иванович",
            date = getDateForDayOffset(11),
            lessonType = LessonType.LECTURE,
            status = LessonStatus.NORMAL,
            lessonMaterials = emptyList(),
            assignment = null
        ),

        // 11 апреля (Суббота)
        Lesson(
            id = 23,
            pairNumber = 1,
            startTime = "10:00",
            endTime = "11:45",
            discipline = "Спортивное право",
            audience = "501",
            originalTeacher = "Иванов Петр Петрович",
            date = getDateForDayOffset(12),
            lessonType = LessonType.LECTURE,
            status = LessonStatus.NORMAL,
            lessonMaterials = emptyList(),
            assignment = null
        ),

        // Воскресенье (12 апреля) - выходной, уроков нет

        // 13 апреля (Понедельник)
        Lesson(
            id = 24,
            pairNumber = 1,
            startTime = "10:00",
            endTime = "11:30",
            discipline = "Международное частное право",
            audience = "405",
            originalTeacher = "Холодов Игорь Павлович",
            date = getDateForDayOffset(14),
            lessonType = LessonType.LECTURE,
            status = LessonStatus.NORMAL,
            lessonMaterials = listOf(
                LessonMaterial("1", "Международное_частное_право.pdf", "2.5 MB", "", "PDF")
            ),
            assignment = Assignment(
                id = "24", title = "Международное частное право",
                description = "Подготовьте доклад о коллизионных нормах.",
                deadline = "10 апреля 2026, 23:59", submitted = false, submittedFiles = emptyList()
            )
        ),
        Lesson(
            id = 25,
            pairNumber = 2,
            startTime = "11:45",
            endTime = "13:15",
            discipline = "Международное частное право",
            audience = "405",
            originalTeacher = "Холодов Игорь Павлович",
            date = getDateForDayOffset(14),
            lessonType = LessonType.SEMINAR,
            status = LessonStatus.NORMAL,
            lessonMaterials = emptyList(),
            assignment = null
        ),

        // 14 апреля (Вторник)
        Lesson(
            id = 26,
            pairNumber = 1,
            startTime = "12:00",
            endTime = "13:30",
            discipline = "Банковское право",
            audience = "302",
            originalTeacher = "Воронова Ольга Дмитриевна",
            date = getDateForDayOffset(15),
            lessonType = LessonType.LECTURE,
            status = LessonStatus.NORMAL,
            lessonMaterials = listOf(
                LessonMaterial("1", "Банковское_право_лекция.pdf", "2.2 MB", "", "PDF")
            ),
            assignment = Assignment(
                id = "26", title = "Банковское право",
                description = "Проанализируйте банковскую систему РФ.",
                deadline = "11 апреля 2026, 23:59", submitted = false, submittedFiles = emptyList()
            )
        ),
        Lesson(
            id = 27,
            pairNumber = 2,
            startTime = "13:45",
            endTime = "15:15",
            discipline = "Налоговое право",
            audience = "402",
            originalTeacher = "Павлов Андрей Игоревич",
            date = getDateForDayOffset(15),
            lessonType = LessonType.SEMINAR,
            status = LessonStatus.SUBSTITUTION,
            substitutionTeacher = "Сидоров Петр Иванович",
            lessonMaterials = listOf(
                LessonMaterial("1", "Налоговое_право_замена.pdf", "1.8 MB", "", "PDF")
            ),
            assignment = Assignment(
                id = "27", title = "Налоговое право (замена)",
                description = "Изучите материалы по налоговым льготам.",
                deadline = "12 апреля 2026, 23:59", submitted = false, submittedFiles = emptyList()
            )
        ),

        // 15 апреля (Среда)
        Lesson(
            id = 28,
            pairNumber = 1,
            startTime = "09:00",
            endTime = "10:30",
            discipline = "Налоговое право",
            audience = "201",
            originalTeacher = "Павлов Андрей Игоревич",
            date = getDateForDayOffset(16),
            lessonType = LessonType.LECTURE,
            status = LessonStatus.CANCELLED,
            lessonMaterials = emptyList(),
            assignment = null
        ),
        Lesson(
            id = 29,
            pairNumber = 2,
            startTime = "10:45",
            endTime = "12:15",
            discipline = "Арбитражный процесс",
            audience = "403",
            originalTeacher = "Наумов Владимир Николаевич",
            date = getDateForDayOffset(16),
            lessonType = LessonType.SEMINAR,
            status = LessonStatus.NORMAL,
            lessonMaterials = emptyList(),
            assignment = null
        ),

        // 16 апреля (Четверг)
        Lesson(
            id = 30,
            pairNumber = 1,
            startTime = "14:00",
            endTime = "15:30",
            discipline = "Арбитражный процесс",
            audience = "403",
            originalTeacher = "Наумов Владимир Николаевич",
            date = getDateForDayOffset(17),
            lessonType = LessonType.LECTURE,
            status = LessonStatus.NORMAL,
            lessonMaterials = emptyList(),
            assignment = null
        ),

        // 17 апреля (Пятница)
        Lesson(
            id = 31,
            pairNumber = 1,
            startTime = "16:00",
            endTime = "17:30",
            discipline = "Право социального обеспечения",
            audience = "207",
            originalTeacher = "Куликова Мария Ивановна",
            date = getDateForDayOffset(18),
            lessonType = LessonType.LECTURE,
            status = LessonStatus.NORMAL,
            lessonMaterials = listOf(
                LessonMaterial("1", "Право_соц_обеспечения.pdf", "2.9 MB", "", "PDF")
            ),
            assignment = Assignment(
                id = "31", title = "Право социального обеспечения",
                description = "Подготовьте реферат о пенсионной системе.",
                deadline = "13 апреля 2026, 23:59", submitted = false, submittedFiles = emptyList()
            )
        ),
        Lesson(
            id = 32,
            pairNumber = 2,
            startTime = "17:45",
            endTime = "19:15",
            discipline = "Право социального обеспечения",
            audience = "207",
            originalTeacher = "Куликова Мария Ивановна",
            date = getDateForDayOffset(18),
            lessonType = LessonType.SEMINAR,
            status = LessonStatus.NORMAL,
            lessonMaterials = emptyList(),
            assignment = null
        ),

        // 18 апреля (Суббота)
        Lesson(
            id = 33,
            pairNumber = 1,
            startTime = "10:00",
            endTime = "11:30",
            discipline = "Спортивное право",
            audience = "501",
            originalTeacher = "Иванов Петр Петрович",
            date = getDateForDayOffset(19),
            lessonType = LessonType.LECTURE,
            status = LessonStatus.NORMAL,
            lessonMaterials = emptyList(),
            assignment = null
        ),

        // Воскресенье (19 апреля) - выходной, уроков нет
    )

    // Получаю дату с фиксированным смещением от понедельника 30.03.2026
    private fun getDateForDayOffset(offset: Long): Instant {
        return fixedDate.plus(java.time.Duration.ofDays(offset))
    }

    override fun getLessonsForDate(date: Instant): Flow<List<Lesson>> {
        val lessonsForDate = mockLessons.filter { lesson ->
            isSameDay(lesson.date, date)
        }.sortedBy { it.pairNumber }
        return flowOf(lessonsForDate)
    }

    override suspend fun getMonthYear(date: Instant): String {
        val localDate = date.atZone(ZoneId.systemDefault()).toLocalDate()
        val month = localDate.month.getDisplayName(TextStyle.FULL, Locale("ru"))
        val year = localDate.year
        // "Апрель 2026" - с заглавной буквы
        return "$month $year".replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }
    }

    override suspend fun getLessonsCountForDate(date: Instant): Int {
        return getLessonsForDate(date).first().size
    }

    override suspend fun getCalendarDates(centerDate: Instant): List<Instant> {
        val localDate = centerDate.atZone(ZoneId.systemDefault()).toLocalDate()
        // Находим понедельник текущей недели
        val monday = localDate.minusDays((localDate.dayOfWeek.value - 1).toLong())

        // Показываем 5 недель (35 дней) для возможности прокрутки
        // 2 недели назад, текущая, 2 недели вперед
        val startDate = monday.minusWeeks(2)

        return (0 until 35).map { offset ->
            startDate.plusDays(offset.toLong())
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
        }
    }

    override suspend fun getDayNumber(date: Instant): String {
        return date.atZone(ZoneId.systemDefault()).toLocalDate()
            .dayOfMonth.toString().padStart(2, '0')
    }

    override suspend fun getDayOfWeek(date: Instant): String {
        val localDate = date.atZone(ZoneId.systemDefault()).toLocalDate()
        return when (localDate.dayOfWeek.value) {
            1 -> "ПН"
            2 -> "ВТ"
            3 -> "СР"
            4 -> "ЧТ"
            5 -> "ПТ"
            6 -> "СБ"
            7 -> "ВС"
            else -> ""
        }
    }

    override suspend fun getLastLessonEndTime(date: Instant): String? {
        val lessons = getLessonsForDate(date).first()
        val lastLesson = lessons.maxByOrNull { it.pairNumber } ?: return null
        return lastLesson.endTime
    }

    override suspend fun getToday(): Instant = Instant.now()

    override suspend fun isToday(date: Instant): Boolean = isSameDay(date, Instant.now())

    override suspend fun isSelected(
        date: Instant,
        selectedDate: Instant
    ): Boolean {
        return isSameDay(date, selectedDate)
    }

    override suspend fun getLessonDetails(lessonId: Int): LessonDetails {
        val lesson = mockLessons.find { it.id == lessonId }
            ?: throw IllegalArgumentException("Lesson not found: $lessonId")

        return LessonDetails(
            lesson = lesson,
            attendanceStatus = AttendanceStatus.NOT_MARKED,
            submittedMaterials = getSubmittedMaterialsForLesson(lessonId),
            lessonMaterials = lesson.lessonMaterials,
            assignment = lesson.assignment
        )
    }

    override suspend fun markAttendance(lessonId: Int, status: AttendanceStatus): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun submitAssignment(lessonId: Int, fileUri: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun getLessonMaterials(lessonId: Int): List<LessonMaterial> {
        return mockLessons.find { it.id == lessonId }?.lessonMaterials ?: emptyList()
    }

    override suspend fun getAssignment(lessonId: Int): Assignment? {
        return mockLessons.find { it.id == lessonId }?.assignment
    }

    private fun getSubmittedMaterialsForLesson(lessonId: Int): List<SubmittedMaterial> {
        return listOf(
            SubmittedMaterial("1", "IMG_2124.jpeg", "1.7 MB", "", "JPG"),
            SubmittedMaterial("2", "IMG_2125.jpeg", "1.7 MB", "", "JPG"),
            SubmittedMaterial("3", "document.pdf", "1.7 MB", "", "PDF")
        )
    }

    private fun isSameDay(date1: Instant, date2: Instant): Boolean {
        val localDate1 = date1.atZone(ZoneId.systemDefault()).toLocalDate()
        val localDate2 = date2.atZone(ZoneId.systemDefault()).toLocalDate()
        return localDate1 == localDate2
    }
}