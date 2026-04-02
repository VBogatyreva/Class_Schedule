package ru.bogatyreva.class_schedule.domain.model

data class LessonDetails(
    val lesson: Lesson,                                                    // Основная информация о занятии
    val attendanceStatus: AttendanceStatus = AttendanceStatus.NOT_MARKED,  // Статус посещаемости студента на этом занятии
    val submittedMaterials: List<SubmittedMaterial> = emptyList(),         // Список материалов, которые студент уже сдал по этому занятию
    val lessonMaterials: List<LessonMaterial> = emptyList(),               // Список материалов, которые преподаватель прикрепил к занятию
    val assignment: Assignment? = null                                     // Задание к занятию (может быть null, если задание не задано)
)

enum class AttendanceStatus {
    NOT_MARKED,      // Не отмечено
    PRESENT,         // Присутствовал
    ABSENT,          // Отсутствовал
    LATE             // Опоздал
}