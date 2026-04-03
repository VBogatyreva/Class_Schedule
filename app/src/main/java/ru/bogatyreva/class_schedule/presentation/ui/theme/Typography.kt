package ru.bogatyreva.class_schedule.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import ru.bogatyreva.class_schedule.R

// Создаем FontFamily для Dela Gothic One
//val DelaGothicOne = FontFamily(
//    Font(R.font.dela_gothic_one_regular, FontWeight.Normal)
//)

// Шрифт Inter
val Inter = FontFamily(
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_semibold, FontWeight.SemiBold),
)

// Создаем кастомную типографику
val AppTypography = Typography(

    // Для заголовка Расписание
    displaySmall = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.5.sp
    ),

    // Стиль для текста месяца - для Март 2026
    labelLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
    ),

    // Стиль для текста Сегодня
    titleSmall = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
    ),

    // Стиль для текста Выходной
    titleLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),

    // Для дней недели (ПН, ВТ, СР, ЧТ, ПТ, СБ, ВС) и чисел в календаре
    bodyLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
    ),

    // Стиль для текста количества занятий
    titleMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ),

    // Для перерыва для текста "перерыв 15 минут" и "11:45 – 12:00"
    labelSmall = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp,
    ),
)

// Для даты "2 апреля 2026"
val DateTextStyle = TextStyle(
    fontFamily = Inter,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.25.sp,
    color = Color(0xFF49454F)
)

// Для интервала часов (13:45)
val LessonTimeTextStyle = TextStyle(
    fontFamily = Inter,
    fontWeight = FontWeight.SemiBold,  // 600
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.1.sp,
    color = LessonTimeText
)

// Для имени преподавателя
val TeacherNameTextStyle = TextStyle(
    fontFamily = Inter,
    fontWeight = FontWeight.SemiBold,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.5.sp,
    color = TitleText
)

// Для инициалов преподавателя
val InitialsTextStyle = TextStyle(
    fontFamily = Inter,
    fontWeight = FontWeight.SemiBold,
    fontSize = 21.sp,
    lineHeight = 22.sp,
    letterSpacing = (-0.43).sp,
    color = Color.White,
    textAlign = TextAlign.Center
)

// Для текста "Место проведения"
val PlaceTitleTextStyle = TextStyle(
    fontFamily = Inter,
    fontWeight = FontWeight.SemiBold, // 600
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.sp,
    color = Color(0xFF79747E)
)

// Для текста "Добавить файл"
val AddFileButtonTextStyle = TextStyle(
    fontFamily = Inter,
    fontWeight = FontWeight.Medium, // 500
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.sp,
    color = AddFile
)

// Для файлов в Задание
val FileNameTextStyle = TextStyle(
    fontFamily = Inter,
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.25.sp,
    color = Color(0xFF1D1B20)
)

// Для размера файла (Body Small)
val FileSizeTextStyle = TextStyle(
    fontFamily = Inter,
    fontWeight = FontWeight.Normal, // 400
    fontSize = 12.sp,
    lineHeight = 16.sp,
    letterSpacing = 0.4.sp,
    color = Color(0xFF79747E)
)

// Для описания темы урока
val LessonDescriptionTextStyle = TextStyle(
    fontFamily = Inter,
    fontWeight = FontWeight.Normal, // 400
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.5.sp,
    color = SummaryTextColor
)

// Для названия темы урока (Body Large SemiBold)
val LessonTitleTextStyle = TextStyle(
    fontFamily = Inter,
    fontWeight = FontWeight.SemiBold, // 600
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.5.sp,
    color = TitleText
)

// Для заголовка "Тема урока" (Body Medium)
val LessonHeaderTextStyle = TextStyle(
    fontFamily = Inter,
    fontWeight = FontWeight.Medium, // 500
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.25.sp,
    color = Color(0xFF79747E)
)