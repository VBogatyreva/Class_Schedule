package ru.bogatyreva.class_schedule.presentation.ui.theme

import androidx.compose.ui.graphics.Color

// Базовые цвета из темы
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// Основные цвета
val White = Color(0xFFFFFFFF)      // #FFFFFF - белый
val Transparent = Color(0x00000000) // #00000000 - прозрачный
val Transparent60 = Color(0x99000000) // #00000000 - прозрачный
val Red = Color(0xFFFF383C)      // #FF383C
val Orange = Color(0xFFFF8D28)   // #FF8D28
val Black = Color(0xFF000000)    // #000000

// Текстовые цвета
val TitleText = Color(0xFF1D1B20) // #1D1B20 - цвет для заголовка "Расписание"
val MonthText = Color(0xFF49454F) // #49454F - цвет для заголовка "Март 2026"
val WeekdaysText = Color(0xFF1D1B20) // #1D1B20 - цвет для дней недели
val WeekendsText = Color(0xFFB3261E) // #B3261E - цвет для выходных (СБ, ВС)
val SubjectText = Color(0xFF000000) // #000000 - для цвета предмета

val LessonTimeText = Color(0xFF3C3C43).copy(alpha = 0.6f)  // #3C3C43 с 60% прозрачности

val InactiveTextColor = Color(0xFF4A4459) // #4A4459 - для цвета текста неактивной сегментированной кнопки
val ActiveTextColor = Color(0xFFFFFFFF)   // #FFFFFF - для цвета текста активной сегментированной кнопки

// Синие оттенки
val BlueToday = Color(0xFF0088FF)   // #0088FF - кнопка "Сегодня"
val BlueSelected = Color(0xFF0088FF) // #0088FF - выделенная дата
val SunIconBackground = Color(0xFF0088FF) // #0088FF - фон на иконку Sun
val BlueScan = Color(0xFF0088FF)   // #0088FF - цвет кнопки "QR"
val AddFile = Color(0xFF34C759)   // #34C759 - кнопка "Добавить файл"

val BorderFocusColor = Color(0xFF0088FF)  // для рамки при фокусе
val BorderUnfocused = Color(0xFFC4C4C7)  //  для рамки без фокуса
val ShadowColor = Color(0xFF3ED1C7)

val ErrorRed = Color(0xFFFF3B30)

// Цвета с прозрачностью (на основе #3C3C43)
val SummaryTextColor = Color(0xFF3C3C43).copy(alpha = 0.6f) // #3C3C43 с 60% прозрачности для текста количество занятий
val TeacherTextColor = Color(0xFF3C3C43).copy(alpha = 0.6f) // #3C3C43 с 60% прозрачности для текста Преподаватель
val RoomNumberTextColor = Color(0xFF3C3C43).copy(alpha = 0.6f) // #3C3C43 с 60% прозрачности для текста Аудитория
val TimeLessonTextColor = Color(0xFF3C3C43).copy(alpha = 0.6f) // #3C3C43 с 60% прозрачности для текста Время урока
val BreakTextColor = Color(0xFF3C3C43).copy(alpha = 0.6f) // #3C3C43 с 60% прозрачности - для текста перемены
val TextColorDetailLesson = Color(0xFF79747E)  // #79747E - для текста Преподаватель, Онлайн, Корпус на экране детальной информации

val RoomColorDetailLesson = Color(0xFF1A1A1A) // #1A1A1A - для текста Место проведения

// Фоновые цвета
val LessonCardColor = Color(0xFFF7F7F7)// #F7F7F7  - фон карточки урока
val LightBg = Color(0xFFFAFAFA) // #FAFAFA - для создания визуального разделения между элементами интерфейса и фона
val ScanBackground = Color(0xFFD9EDFF) // #D9EDFF  - фон иконки QR
val AvatarBackground = Color(0xFFA9A7A7)  // #A9A7A7 - фон аватара

val InactiveBackgroundColor = Color(0xFF1D1B20).copy(alpha = 0.08f) // #1D1B20 с 8% прозрачности - для неактивной сегментированной кнопки
val ActiveBackgroundColor = Color(0xFF0088FF)   // #0088FF - для активной сегментированной кнопки


// Теги занятий
val TagNumberBackground = Color(0xFF787878).copy(alpha = 0.2f) // #787878 с 20% прозрачности  - для номера занятия
val TagLessonTypeLecture = Color(0xFF00C0E8)// #00C0E8 - для типа занятия лекция
val TagLessonTypeSeminar = Color(0xFF00C8B3)// #00C8B3 - для типа занятия семинара
val TagLessonCancel = Color(0xFFFF383C)// #FF383C - для отмена занятия
val TagStatusSubstitution = Color(0xFFFF8D28) // #FF8D28 - для статуса замена
val TagLessonTypeTest = Color(0xFF6155F5) // #6155F5 - для статуса зачета
val TagLessonTypeExam = Color(0xFFFF2D55) // #FF2D55 - для статуса экзамена

// Дополнительные цвета

val Separator = Color(0xFFE6E6E6)  // #E6E6E6 - цвет разделителя
val Muted = Color(0xFF9E9E9E) // #9E9E9E - для неактивных элементов интерфейса
val DrillInColor = Color(0xFFC4C4C7) // #C4C4C7 - для иконки перехода