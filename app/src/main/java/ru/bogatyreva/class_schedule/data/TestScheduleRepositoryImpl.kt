package ru.bogatyreva.class_schedule.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ru.bogatyreva.class_schedule.domain.Lesson
import ru.bogatyreva.class_schedule.domain.ScheduleRepository
import ru.bogatyreva.class_schedule.domain.Semester
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

        // Понедельник
        Lesson(
            id = 1,
            pairNumber = 1,
            startTime = "8:00",
            endTime = "8:45",
            discipline = "Математика",
            audience = "Каб. 203",
            teacher = "Морозова Н.Д.",
            date = getDateForDayOffset(0)
        ),
        Lesson(
            id = 2,
            pairNumber = 2,
            startTime = "9:00",
            endTime = "9:45",
            discipline = "Физика",
            audience = "Каб. 305",
            teacher = "Смирнов В.В.",
            date = getDateForDayOffset(0)
        ),
        Lesson(
            id = 3,
            pairNumber = 3,
            startTime = "10:00",
            endTime = "10:45",
            discipline = "Информатика",
            audience = "Каб. 404",
            teacher = "Козлов А.А.",
            date = getDateForDayOffset(0)
        ),

        // Вторник
        Lesson(
            id = 4,
            pairNumber = 1,
            startTime = "8:00",
            endTime = "8:45",
            discipline = "Русский язык",
            audience = "Каб. 201",
            teacher = "Сидорова Е.В.",
            date = getDateForDayOffset(1)
        ),
        Lesson(
            id = 5,
            pairNumber = 2,
            startTime = "9:00",
            endTime = "9:45",
            discipline = "Литература",
            audience = "Каб. 201",
            teacher = "Сидорова Е.В.",
            date = getDateForDayOffset(1)
        ),
        Lesson(
            id = 6,
            pairNumber = 3,
            startTime = "10:00",
            endTime = "10:45",
            discipline = "Английский язык",
            audience = "Каб. 307",
            teacher = "Морозова Н.Д.",
            date = getDateForDayOffset(1)
        ),
        Lesson(
            id = 7,
            pairNumber = 4,
            startTime = "11:00",
            endTime = "11:45",
            discipline = "Немецкий язык",
            audience = "Каб. 308",
            teacher = "Шмидт Т.Т.",
            date = getDateForDayOffset(1)
        ),

        // Среда
        Lesson(
            id = 8,
            pairNumber = 1,
            startTime = "8:00",
            endTime = "8:45",
            discipline = "История",
            audience = "Каб. 101",
            teacher = "Соколов Д.И.",
            date = getDateForDayOffset(2)
        ),
        Lesson(
            id = 9,
            pairNumber = 2,
            startTime = "9:00",
            endTime = "9:45",
            discipline = "Химия",
            audience = "Каб. 303",
            teacher = "Смирнова Е.А.",
            date = getDateForDayOffset(2)
        ),
        Lesson(
            id = 10,
            pairNumber = 3,
            startTime = "10:00",
            endTime = "10:45",
            discipline = "Биология",
            audience = "Каб. 202",
            teacher = "Николаева М.И.",
            date = getDateForDayOffset(2)
        ),

        // Четверг
        Lesson(
            id = 11,
            pairNumber = 1,
            startTime = "8:00",
            endTime = "8:45",
            discipline = "Математика",
            audience = "Каб. 203",
            teacher = "Морозова Н.Д.",
            date = getDateForDayOffset(3)
        ),
        Lesson(
            id = 12,
            pairNumber = 2,
            startTime = "9:00",
            endTime = "9:45",
            discipline = "История",
            audience = "Каб. 209",
            teacher = "Здравковский А.П.",
            date = getDateForDayOffset(3)
        ),
        Lesson(
            id = 13,
            pairNumber = 3,
            startTime = "10:00",
            endTime = "10:45",
            discipline = "Математика",
            audience = "Каб. 203",
            teacher = "Морозова Н.Д.",
            date = getDateForDayOffset(3)
        ),
        Lesson(
            id = 14,
            pairNumber = 4,
            startTime = "11:00",
            endTime = "11:45",
            discipline = "Литература",
            audience = "Каб. 203",
            teacher = "Степаненко Е.Б.",
            date = getDateForDayOffset(3)
        ),

        //Пятница
        Lesson(
            id = 15,
            pairNumber = 1,
            startTime = "8:00",
            endTime = "8:45",
            discipline = "Философия",
            audience = "Каб. 209",
            teacher = "Иванова М.И.",
            date = getDateForDayOffset(4)
        ),
        Lesson(
            id = 16,
            pairNumber = 2,
            startTime = "9:00",
            endTime = "9:45",
            discipline = "Философия",
            audience = "Каб. 209",
            teacher = "Иванова М.И.",
            date = getDateForDayOffset(4)
        ),
        Lesson(
            id = 17,
            pairNumber = 3,
            startTime = "10:00",
            endTime = "10:45",
            discipline = "Экономика",
            audience = "Каб. 201",
            teacher = "Петрова А.Н.",
            date = getDateForDayOffset(4)
        ),

        // Суббота
        Lesson(
            id = 18,
            pairNumber = 1,
            startTime = "9:00",
            endTime = "9:45",
            discipline = "Физкультура",
            audience = "Спортзал",
            teacher = "Иванов П.П.",
            date = getDateForDayOffset(5)
        ),
        Lesson(
            id = 19,
            pairNumber = 2,
            startTime = "10:00",
            endTime = "10:45",
            discipline = "Физкультура",
            audience = "Спортзал",
            teacher = "Иванов П.П.",
            date = getDateForDayOffset(5)
        ),

        // Воскресенье - выходной, уроков нет
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

    override suspend fun getFormattedDate(date: Instant): String {
        val localDate = date.atZone(ZoneId.systemDefault()).toLocalDate()
        val day = localDate.dayOfMonth
        val month = localDate.month.getDisplayName(TextStyle.FULL, Locale("ru"))
        val dayOfWeek = localDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("ru"))

        return "$day $month, $dayOfWeek"
    }

    override suspend fun getSemesterDisplayName(): String {
        val now = LocalDate.now()
        val year = now.year
        val semester = if (now.monthValue in 9..12) Semester.FALL else Semester.SPRING
        val semesterName = if (semester == Semester.FALL) "Осенний" else "Весенний"

        return "$semesterName семестр $year год"
    }

    override suspend fun getLessonsCountForDate(date: Instant): Int {
        return mockLessons.count { isSameDay(it.date, date) }
    }

    override suspend fun getCalendarDates(centerDate: Instant): List<Instant> {
        val localDate = centerDate.atZone(ZoneId.systemDefault()).toLocalDate()
        val monday = localDate.minusDays((localDate.dayOfWeek.value - 1).toLong())

        return (0 until 7).map { offset ->
            monday.plusDays(offset.toLong())
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
            1 -> "Пн"
            2 -> "Вт"
            3 -> "Ср"
            4 -> "Чт"
            5 -> "Пт"
            6 -> "Сб"
            7 -> "Вс"
            else -> ""
        }
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