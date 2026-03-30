package ru.bogatyreva.class_schedule.presentation.ui.theme

import androidx.compose.ui.graphics.Color

// Material Design цвета (стандартные)
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// Цвета из дизайна

val White = Color.White
val Transparent = Color.Transparent
val Red = Color(0xFFFF383C)      // #FF383C

val Orange = Color(0xFFFF8D28)  // #FF8D28

val TitleText = Color(0xFF1D1B20) // #1D1B20 - цвет для заголовка "Расписание"

val MonthText = Color(0xFF49454F) // #49454F - цвет для заголовка "Март 2026"

val WeekdaysText = Color(0xFF1D1B20) // #1D1B20 - цвет для заголовка и дней недели
val WeekendsText = Color(0xFFB3261E) // #B3261E - цвет для выходных (СБ, ВС)

val BlueToday = Color(0xFF0088FF)   // #0088FF - кнопка "Сегодня"
val BlueSelected = Color(0xFF0088FF) // #0088FF - выделенная дата

val SunIconBackground = Color(0xFF0088FF) // #0088FF - фон на иконку Sun

val SummaryTextColor = Color(0xFF3C3C43).copy(alpha = 0.6f) // #3C3C43 с 60% прозрачности для текста количество занятий

// Новые цвета для карточки урока

val LessonCardColor = Color(0xFFF7F7F7)// #F7F7F7
val TagNumberBackground = Color(0xFF787878).copy(alpha = 0.2f) // #787878 с 20% прозрачности
val TagLessonTypeLecture = Color(0xFF00C0E8)// #00C0E8 - для типа занятия лекция
val TagLessonTypeSeminar = Color(0xFF00C8B3)// #00C8B3 - для типа занятия семинара
val TagLessonCancel = Color(0xFFFF383C)// #FF383C - для отмена занятия
val TagStatusSubstitution = Color(0xFFFF8D28) // #FF8D28 - для статуса "замена"

val TagLessonTypeTest = Color(0xFF6155F5) // #6155F5 - для статуса зачета
val TagLessonTypeExam = Color(0xFFFF2D55) // #FF2D55 - для статуса экзамена
val SubjectText = Color(0xFF000000) // #000000 - для цвета предмета
val TeacherTextColor = Color(0xFF3C3C43).copy(alpha = 0.6f) // #3C3C43 с 60% прозрачности для текста Преподаватель
val RoomNumberTextColor = Color(0xFF3C3C43).copy(alpha = 0.6f) // #3C3C43 с 60% прозрачности для текста Аудитория
val TimeLessonTextColor = Color(0xFF3C3C43).copy(alpha = 0.6f) // #3C3C43 с 60% прозрачности для текста Время урока
val DrillInColor = Color(0xFFC4C4C7) // #C4C4C7 - для иконки перехода
val SecondaryText = Color(0xFF3C3C43).copy(alpha = 0.6f) // #3C3C43 с 60% прозрачности

val BreakTextColor = Color(0xFF3C3C43).copy(alpha = 0.6f) // #3C3C43 с 60% прозрачности - для текста перемены

val BlueScan = Color(0xFF0088FF)   // #0088FF - кнопка "QR"

val LightBg = Color(0xFFFAFAFA) // #FAFAFA
val Muted = Color(0xFF9E9E9E) // #9E9E9E
val ScanBackground = Color(0xFFD9EDFF) // #D9EDFF

val BluePrimary = Color(0xFF2196F3) // #2196F3 - синий (основной цвет)
val LightGray = Color(0xFFF5F5F5) // #F5F5F5 - светло-серый для фона
val DividerGray = Color(0xFFE0E0E0) // #E0E0E0 - серый для разделителей

// State layer цвета
val SelectedStateLayer = Color.White.copy(alpha = 0.10f) // Белый с 10% прозрачности для эффекта нажатия
