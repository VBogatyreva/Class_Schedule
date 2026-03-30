package ru.bogatyreva.class_schedule.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import ru.bogatyreva.class_schedule.domain.Lesson
import ru.bogatyreva.class_schedule.domain.LessonStatus
import ru.bogatyreva.class_schedule.domain.LessonType
import ru.bogatyreva.class_schedule.domain.ScheduleRepository
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale

class TestScheduleRepositoryImpl : ScheduleRepository {

    // Фиксирую дату для тестовых данных на: Понедельник, 16 марта 2026 года
    private val fixedDate = LocalDate.of(2026, 3, 16)
        .atStartOfDay(ZoneId.systemDefault()) // Начало дня (00:00)
        .toInstant()

    // Расписание на неделю:
    private val mockLessons = listOf(

        // Понедельник (9 марта)
        Lesson(
            id = 1,
            pairNumber = 1,
            startTime = "09:00",
            endTime = "10:30",
            discipline = "История государства и права",
            audience = "101",
            originalTeacher = "Сергеев Сергей Сергеевич",
            date = getDateForDayOffset(-7),
            lessonType = LessonType.LECTURE,
            status = LessonStatus.NORMAL,
            substitutionTeacher = null,
            nextLessonStartTime = "10:45"
        ),
        Lesson(
            id = 2,
            pairNumber = 2,
            startTime = "10:45",
            endTime = "12:15",
            discipline = "Спортивное право",
            audience = "501",
            originalTeacher = "Иванов Петр Петрович",
            date = getDateForDayOffset(-7),
            lessonType = LessonType.SEMINAR,
            status = LessonStatus.NORMAL,
            substitutionTeacher = null,
            nextLessonStartTime = null
        ),

        // Вторник (10 марта)
        Lesson(
            id = 3,
            pairNumber = 1,
            startTime = "10:00",
            endTime = "11:30",
            discipline = "Римское право",
            audience = "203",
            originalTeacher = "Романова Елена Викторовна",
            date = getDateForDayOffset(-6),
            lessonType = LessonType.LECTURE,
            status = LessonStatus.NORMAL,
            substitutionTeacher = null,
            nextLessonStartTime = "11:45"
        ),
        Lesson(
            id = 4,
            pairNumber = 2,
            startTime = "11:45",
            endTime = "13:15",
            discipline = "Международное право",
            audience = "405",
            originalTeacher = "Иванов Иван Иванович",
            date = getDateForDayOffset(-6),
            lessonType = LessonType.SEMINAR,
            status = LessonStatus.SUBSTITUTION,
            substitutionTeacher = "замена: Кузнецов Андрей Владимирович",
            nextLessonStartTime = null
        ),

        // Среда (11 марта)
        Lesson(
            id = 5,
            pairNumber = 1,
            startTime = "14:00",
            endTime = "15:30",
            discipline = "Конституционное право",
            audience = "305",
            originalTeacher = "Константинов Павел Андреевич",
            date = getDateForDayOffset(-5),
            lessonType = LessonType.LECTURE,
            status = LessonStatus.NORMAL,
            substitutionTeacher = null,
            nextLessonStartTime = null
        ),

        // Четверг (12 марта)
        Lesson(
            id = 6,
            pairNumber = 1,
            startTime = "09:00",
            endTime = "10:30",
            discipline = "Криминалистика",
            audience = "402",
            originalTeacher = "Ветрова Анна Сергеевна",
            date = getDateForDayOffset(-4),
            lessonType = LessonType.LECTURE,
            status = LessonStatus.CANCELLED,
            substitutionTeacher = null,
            nextLessonStartTime = null
        ),

        // Пятница (13 марта)
        Lesson(
            id = 7,
            pairNumber = 1,
            startTime = "13:00",
            endTime = "14:30",
            discipline = "Судебная медицина",
            audience = "501",
            originalTeacher = "Волков Сергей Петрович",
            date = getDateForDayOffset(-3),
            lessonType = LessonType.LECTURE,
            status = LessonStatus.NORMAL,
            substitutionTeacher = null,
            nextLessonStartTime = null
        ),

        // Суббота (14 марта) - выходной, уроков нет
        // Воскресенье (15 марта) - выходной, уроков нет

        // Понедельник (16 марта)
        Lesson(
            id = 8,
            pairNumber = 1,
            startTime = "10:00",
            endTime = "11:45",
            discipline = "Международное право",
            audience = "405",
            originalTeacher = "Иванов Иван Иванович",
            date = getDateForDayOffset(0),
            lessonType = LessonType.LECTURE,
            status = LessonStatus.NORMAL,
            substitutionTeacher = null,
            nextLessonStartTime = "10:45"
        ),

        Lesson(
            id = 9,
            pairNumber = 2,
            startTime = "12:00",
            endTime = "13:45",
            discipline = "Государственное регулирование",
            audience = "306",
            originalTeacher = "Петров Петр Петрович",
            date = getDateForDayOffset(0),
            lessonType = LessonType.SEMINAR,
            status = LessonStatus.NORMAL,
            substitutionTeacher = null,
            nextLessonStartTime = "12:45"
        ),

        Lesson(
            id = 10,
            pairNumber = 3,
            startTime = "14:00",
            endTime = "15:45",
            discipline = "Уголовное право",
            audience = "205",
            originalTeacher = "Смирнов Алексей Игоревич",
            date = getDateForDayOffset(0),
            lessonType = LessonType.LECTURE,
            status = LessonStatus.CANCELLED,
            substitutionTeacher = null,
            nextLessonStartTime = null
        ),

        Lesson(
            id = 11,
            pairNumber = 4,
            startTime = "16:00",
            endTime = "17:45",
            discipline = "Итория России",
            audience = "408",
            originalTeacher = "Васильев Игорь Александрович",
            date = getDateForDayOffset(0),
            lessonType = LessonType.EXAM,
            status = LessonStatus.CANCELLED,
            substitutionTeacher = null,
            nextLessonStartTime = null
        ),

        // Вторник (17 марта)
        Lesson(
            id = 12,
            pairNumber = 1,
            startTime = "10:00",
            endTime = "11:45",
            discipline = "Гражданское право",
            audience = "301",
            originalTeacher = "Петрова Елена Сергеевна",
            date = getDateForDayOffset(1),
            lessonType = LessonType.EXAM,
            status = LessonStatus.NORMAL,
            substitutionTeacher = null,
            nextLessonStartTime = "11:45"
        ),
        Lesson(
            id = 13,
            pairNumber = 2,
            startTime = "12:00",
            endTime = "13:45",
            discipline = "Административное право",
            audience = "302",
            originalTeacher = "Соколов Дмитрий Иванович",
            date = getDateForDayOffset(1),
            lessonType = LessonType.LECTURE,
            status = LessonStatus.NORMAL,
            substitutionTeacher = null,
            nextLessonStartTime = null
        ),

        Lesson(
            id = 14,
            pairNumber = 3,
            startTime = "14:00",
            endTime = "15:45",
            discipline = "Философия",
            audience = "103",
            originalTeacher = "Кравцова Ирина Андреевна",
            date = getDateForDayOffset(1),
            lessonType = LessonType.SEMINAR,
            status = LessonStatus.NORMAL,
            substitutionTeacher = null,
            nextLessonStartTime = null
        ),

        // Среда (18 марта)
        Lesson(
            id = 15,
            pairNumber = 1,
            startTime = "10:00",
            endTime = "11:45",
            discipline = "Экологическое право",
            audience = "201",
            originalTeacher = "Козлова Анна Александровна",
            date = getDateForDayOffset(2),
            lessonType = LessonType.TEST,
            status = LessonStatus.NORMAL,
            substitutionTeacher = null,
            nextLessonStartTime = "10:45"
        ),

        Lesson(
            id = 16,
            pairNumber = 2,
            startTime = "12:00",
            endTime = "13:45",
            discipline = "Социология",
            audience = "608",
            originalTeacher = "Орлова Татьяна Васильевна",
            date = getDateForDayOffset(2),
            lessonType = LessonType.LECTURE,
            status = LessonStatus.NORMAL,
            substitutionTeacher = null,
            nextLessonStartTime = null
        ),
        Lesson(
            id = 17,
            pairNumber = 3,
            startTime = "14:00",
            endTime = "15:45",
            discipline = "Трудовое право",
            audience = "202",
            originalTeacher = "Николаев Петр Сергеевич",
            date = getDateForDayOffset(2),
            lessonType = LessonType.SEMINAR,
            status = LessonStatus.NORMAL,
            substitutionTeacher = null,
            nextLessonStartTime = null
        ),

        // Четверг (19 марта)
        Lesson(
            id = 18,
            pairNumber = 1,
            startTime = "10:00",
            endTime = "11:45",
            discipline = "Финансовое право",
            audience = "401",
            originalTeacher = "Зайцева Ольга Викторовна",
            date = getDateForDayOffset(3),
            lessonType = LessonType.LECTURE,
            status = LessonStatus.NORMAL,
            substitutionTeacher = null,
            nextLessonStartTime = null
        ),

        Lesson(
            id = 19,
            pairNumber = 2,
            startTime = "12:00",
            endTime = "13:45",
            discipline = "Государственное регулирование",
            audience = "306",
            originalTeacher = "Петров Петр Петрович",
            date = getDateForDayOffset(3),
            lessonType = LessonType.SEMINAR,
            status = LessonStatus.SUBSTITUTION,
            substitutionTeacher = "замена: Белов Михаил Павлович",
            nextLessonStartTime = "12:45"
        ),

        Lesson(
            id = 20,
            pairNumber = 3,
            startTime = "14:00",
            endTime = "15:45",
            discipline = "Государственное регулирование",
            audience = "306",
            originalTeacher = "Петров Петр Петрович",
            date = getDateForDayOffset(3),
            lessonType = LessonType.SEMINAR,
            status = LessonStatus.NORMAL,
            substitutionTeacher = null,
            nextLessonStartTime = "12:45"
        ),

        // Пятница (20 марта)
        Lesson(
            id = 21,
            pairNumber = 1,
            startTime = "10:00",
            endTime = "11:45",
            discipline = "Налоговое право",
            audience = "402",
            originalTeacher = "Павлов Андрей Игоревич",
            date = getDateForDayOffset(4),
            lessonType = LessonType.SEMINAR,
            status = LessonStatus.NORMAL,
            substitutionTeacher = null,
            nextLessonStartTime = null
        ),

        Lesson(
            id = 22,
            pairNumber = 2,
            startTime = "12:00",
            endTime = "13:45",
            discipline = "Административное право",
            audience = "302",
            originalTeacher = "Соколов Дмитрий Иванович",
            date = getDateForDayOffset(4),
            lessonType = LessonType.LECTURE,
            status = LessonStatus.NORMAL,
            substitutionTeacher = null,
            nextLessonStartTime = null
        ),

        // Суббота (21 марта)
        Lesson(
            id = 23,
            pairNumber = 1,
            startTime = "10:00",
            endTime = "11:45",
            discipline = "Спортивное право",
            audience = "501",
            originalTeacher = "Иванов Петр Петрович",
            date = getDateForDayOffset(5),
            lessonType = LessonType.LECTURE,
            status = LessonStatus.NORMAL,
            substitutionTeacher = null,
            nextLessonStartTime = null
        ),

        // Воскресенье (22 марта) - выходной, уроков нет

        // Понедельник (23 марта)
        Lesson(
            id = 24,
            pairNumber = 1,
            startTime = "10:00",
            endTime = "11:30",
            discipline = "Международное частное право",
            audience = "405",
            originalTeacher = "Холодов Игорь Павлович",
            date = getDateForDayOffset(7),
            lessonType = LessonType.LECTURE,
            status = LessonStatus.NORMAL,
            substitutionTeacher = null,
            nextLessonStartTime = "11:45"
        ),
        Lesson(
            id = 25,
            pairNumber = 2,
            startTime = "11:45",
            endTime = "13:15",
            discipline = "Международное частное право",
            audience = "405",
            originalTeacher = "Холодов Игорь Павлович",
            date = getDateForDayOffset(7),
            lessonType = LessonType.SEMINAR,
            status = LessonStatus.NORMAL,
            substitutionTeacher = null,
            nextLessonStartTime = null
        ),

        // Вторник (24 марта)
        Lesson(
            id = 26,
            pairNumber = 1,
            startTime = "12:00",
            endTime = "13:30",
            discipline = "Банковское право",
            audience = "302",
            originalTeacher = "Воронова Ольга Дмитриевна",
            date = getDateForDayOffset(8),
            lessonType = LessonType.LECTURE,
            status = LessonStatus.NORMAL,
            substitutionTeacher = null,
            nextLessonStartTime = "13:45"
        ),
        Lesson(
            id = 27,
            pairNumber = 2,
            startTime = "13:45",
            endTime = "15:15",
            discipline = "Налоговое право",
            audience = "402",
            originalTeacher = "Павлов Андрей Игоревич",
            date = getDateForDayOffset(8),
            lessonType = LessonType.SEMINAR,
            status = LessonStatus.SUBSTITUTION,
            substitutionTeacher = "замена: Сидоров Петр Иванович",
            nextLessonStartTime = null
        ),

        // Среда (25 марта)
        Lesson(
            id = 28,
            pairNumber = 1,
            startTime = "09:00",
            endTime = "10:30",
            discipline = "Налоговое право",
            audience = "201",
            originalTeacher = "Павлов Андрей Игоревич",
            date = getDateForDayOffset(9),
            lessonType = LessonType.LECTURE,
            status = LessonStatus.CANCELLED,
            substitutionTeacher = null,
            nextLessonStartTime = null
        ),
        Lesson(
            id = 29,
            pairNumber = 2,
            startTime = "10:45",
            endTime = "12:15",
            discipline = "Арбитражный процесс",
            audience = "403",
            originalTeacher = "Наумов Владимир Николаевич",
            date = getDateForDayOffset(9),
            lessonType = LessonType.SEMINAR,
            status = LessonStatus.NORMAL,
            substitutionTeacher = null,
            nextLessonStartTime = null
        ),

        // Четверг (26 марта)
        Lesson(
            id = 30,
            pairNumber = 1,
            startTime = "14:00",
            endTime = "15:30",
            discipline = "Арбитражный процесс",
            audience = "403",
            originalTeacher = "Наумов Владимир Николаевич",
            date = getDateForDayOffset(10),
            lessonType = LessonType.LECTURE,
            status = LessonStatus.NORMAL,
            substitutionTeacher = null,
            nextLessonStartTime = null
        ),

        // Пятница (27 марта)
        Lesson(
            id = 31,
            pairNumber = 1,
            startTime = "16:00",
            endTime = "17:30",
            discipline = "Право социального обеспечения",
            audience = "207",
            originalTeacher = "Куликова Мария Ивановна",
            date = getDateForDayOffset(11),
            lessonType = LessonType.LECTURE,
            status = LessonStatus.NORMAL,
            substitutionTeacher = null,
            nextLessonStartTime = "17:45"
        ),
        Lesson(
            id = 32,
            pairNumber = 2,
            startTime = "17:45",
            endTime = "19:15",
            discipline = "Право социального обеспечения",
            audience = "207",
            originalTeacher = "Куликова Мария Ивановна",
            date = getDateForDayOffset(11),
            lessonType = LessonType.SEMINAR,
            status = LessonStatus.NORMAL,
            substitutionTeacher = null,
            nextLessonStartTime = null
        ),

        // Суббота (28 марта)
        Lesson(
            id = 33,
            pairNumber = 1,
            startTime = "10:00",
            endTime = "11:30",
            discipline = "Спортивное право",
            audience = "501",
            originalTeacher = "Иванов Петр Петрович",
            date = getDateForDayOffset(12),
            lessonType = LessonType.LECTURE,
            status = LessonStatus.NORMAL,
            substitutionTeacher = null,
            nextLessonStartTime = null
        ),

        // Воскресенье (29 марта) - выходной, уроков нет
    )

    // Получаю дату с фиксированным смещением от понедельника 16.03.2026
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

    private fun isSameDay(date1: Instant, date2: Instant): Boolean {
        val localDate1 = date1.atZone(ZoneId.systemDefault()).toLocalDate()
        val localDate2 = date2.atZone(ZoneId.systemDefault()).toLocalDate()
        return localDate1 == localDate2
    }
}