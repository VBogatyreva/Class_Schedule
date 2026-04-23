package ru.bogatyreva.class_schedule.presentation.screens.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.bogatyreva.class_schedule.R
import ru.bogatyreva.class_schedule.domain.model.Lesson
import ru.bogatyreva.class_schedule.domain.model.LessonStatus
import ru.bogatyreva.class_schedule.domain.model.LessonType
import ru.bogatyreva.class_schedule.presentation.ui.theme.Black
import ru.bogatyreva.class_schedule.presentation.ui.theme.BlueScan
import ru.bogatyreva.class_schedule.presentation.ui.theme.BlueSelected
import ru.bogatyreva.class_schedule.presentation.ui.theme.BlueToday
import ru.bogatyreva.class_schedule.presentation.ui.theme.BreakTextColor
import ru.bogatyreva.class_schedule.presentation.ui.theme.DrillInColor
import ru.bogatyreva.class_schedule.presentation.ui.theme.LessonCardColor
import ru.bogatyreva.class_schedule.presentation.ui.theme.LightBg
import ru.bogatyreva.class_schedule.presentation.ui.theme.MonthText
import ru.bogatyreva.class_schedule.presentation.ui.theme.Muted
import ru.bogatyreva.class_schedule.presentation.ui.theme.RoomNumberTextColor
import ru.bogatyreva.class_schedule.presentation.ui.theme.ScanBackground
import ru.bogatyreva.class_schedule.presentation.ui.theme.SubjectText
import ru.bogatyreva.class_schedule.presentation.ui.theme.SummaryTextColor
import ru.bogatyreva.class_schedule.presentation.ui.theme.SunIconBackground
import ru.bogatyreva.class_schedule.presentation.ui.theme.TagLessonCancel
import ru.bogatyreva.class_schedule.presentation.ui.theme.TagLessonTypeExam
import ru.bogatyreva.class_schedule.presentation.ui.theme.TagLessonTypeLecture
import ru.bogatyreva.class_schedule.presentation.ui.theme.TagLessonTypeSeminar
import ru.bogatyreva.class_schedule.presentation.ui.theme.TagLessonTypeTest
import ru.bogatyreva.class_schedule.presentation.ui.theme.TagNumberBackground
import ru.bogatyreva.class_schedule.presentation.ui.theme.TagStatusSubstitution
import ru.bogatyreva.class_schedule.presentation.ui.theme.TeacherTextColor
import ru.bogatyreva.class_schedule.presentation.ui.theme.TimeLessonTextColor
import ru.bogatyreva.class_schedule.presentation.ui.theme.TitleText
import ru.bogatyreva.class_schedule.presentation.ui.theme.Transparent
import ru.bogatyreva.class_schedule.presentation.ui.theme.WeekdaysText
import ru.bogatyreva.class_schedule.presentation.ui.theme.WeekendsText
import ru.bogatyreva.class_schedule.presentation.ui.theme.White
import ru.bogatyreva.class_schedule.utils.isSameDay
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel = viewModel(),
    onQrCodeClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onScheduleClick: () -> Unit = {},
    onCareerClick: () -> Unit = {},
    onLessonClick: (Int) -> Unit = {},
    onLogoutClick: () -> Unit = {},
    funDialog: @Composable () -> Unit = { }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val calendarDates by viewModel.calendarDates.collectAsStateWithLifecycle()

    // Получаем ширину экрана для расчета
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    // Рассчитываем ширину одного дня так, чтобы 7 дней точно поместились
    val dayWidth = (screenWidth - 32.dp) / 7 // 32.dp = horizontal padding (16.dp * 2)

    // Состояние для списка
    val listState = rememberLazyListState()

    // Флаг для предотвращения циклической прокрутки - на будущее для бесконечной прокрутки календаря
    var isProgrammaticScroll by remember { mutableStateOf(false) }


    // Прокручиваем к выбранной дате - но в начало ставим понедельник
    LaunchedEffect(state.selectedDate) {
        if (calendarDates.isNotEmpty()) {
            // Находим понедельник недели для выбранной даты
            val localDate = state.selectedDate.atZone(ZoneId.systemDefault()).toLocalDate()
            // Вычисляем понедельник (dayOfWeek.value: 1 = ПН, 7 = ВС)
            val monday = localDate.minusDays((localDate.dayOfWeek.value - 1).toLong())
            val mondayInstant = monday.atStartOfDay(ZoneId.systemDefault()).toInstant()

            // Ищем индекс этого понедельника в списке
            val mondayIndex = calendarDates.indexOfFirst { date ->
                isSameDay(date, mondayInstant)
            }

            if (mondayIndex >= 0) {
                isProgrammaticScroll = true
                // Прокручиваем к понедельнику (он будет в начале экрана)
                listState.animateScrollToItem(mondayIndex)
                isProgrammaticScroll = false
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .statusBarsPadding(),

        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Расписание",
                        style = MaterialTheme.typography.displaySmall,
                        color = TitleText,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .padding(start = 16.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
            )
        },

        bottomBar = {
            // Нижняя панель
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(LightBg)
                    .border(
                        width = 1.dp,
                        color = Color.Black.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp)
                    )
                    .navigationBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 1. Расписание
                    BottomNavItem(
                        modifier = Modifier.weight(1f)
                            .width(70.dp)
                            .height(56.dp),
                        isActive = true,
                        icon = R.drawable.ic_calendar,
                        label = "Расписание",
                        onClick = onScheduleClick
                    )

                    // 2. Карьера
                    BottomNavItem(
                        modifier = Modifier.weight(1f)
                            .width(70.dp)
                            .height(56.dp),
                        isActive = false,
                        icon = R.drawable.ic_career,
                        label = "Карьера",
                        onClick = onCareerClick
                    )

                    // 3. QR-сканер
                    BottomNavItem(
                        modifier = Modifier.weight(1f)
                            .width(70.dp)
                            .height(56.dp),
                        isActive = false,
                        icon = R.drawable.ic_scan,
                        label = "Скан QR",
                        onClick = onQrCodeClick
                    )

                    // 4. Профиль
                    BottomNavItem(
                        modifier = Modifier.weight(1f)
                            .width(70.dp)
                            .height(56.dp),
                        isActive = false,
                        icon = R.drawable.ic_profile,
                        label = "Профиль",
                        onClick = onProfileClick
                    )
                }
            }
        }

    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(
                            top = 4.dp,
                            bottom = 4.dp,
                            start = 16.dp,
                            end = 12.dp
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Март 2026
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .height(40.dp)
                            .width(102.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        Text(
                            text = formatMonthYear(state.selectedDate),
                            style = MaterialTheme.typography.labelLarge,
                            color = MonthText,
                            modifier = Modifier
                                .padding(
                                    top = 10.dp,
                                    bottom = 10.dp,
                                    start = 8.dp
                                )
                                .align(Alignment.Center)
                        )
                    }

                    // СЕГОДНЯ - появляется только когда выбран не сегодняшний день
                    Box(
                        modifier = Modifier
                            .height(40.dp)
                            .width(102.dp)
                            .align(Alignment.CenterVertically),
                        contentAlignment = Alignment.Center
                    ) {
                        if (state.showTodayButton) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(100.dp))
                                    .pointerInput(Unit) {
                                        detectTapGestures(onTap = {
                                            viewModel.processCommand(ScheduleCommands.GoToToday)
                                        })
                                    }
                                    .height(40.dp)
                                    .width(97.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(
                                            start = 8.dp,
                                            top = 10.dp,
                                            bottom = 10.dp,
                                            end = 4.dp
                                        ),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Текст Сегодня
                                    Text(
                                        text = "Сегодня",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = BlueToday,
                                        modifier = Modifier
                                            .width(59.dp)
                                            .height(20.dp)
                                    )
                                    // Иконка "Сегодня" (треугольник/стрелка)
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_today_navigation),
                                        contentDescription = null,
                                        tint = BlueToday,
                                        modifier = Modifier.size(
                                            4.dp,
                                            8.dp
                                        )
                                    )
                                }
                            }
                        }
                        // Если кнопки нет, Box остается пустым, но с теми же размерами
                        // тогда элементы на экране не прыгают вверх
                    }
                }

                // Календарь
                LazyRow(
                    state = listState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(96.dp),
                    horizontalArrangement = Arrangement.spacedBy(0.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    itemsIndexed(calendarDates) { index, date ->
                        val localDate = date.atZone(ZoneId.systemDefault()).toLocalDate()
                        val dayNumber = localDate.dayOfMonth.toString()
                        val dayOfWeek = when (localDate.dayOfWeek.value) {
                            1 -> "ПН"
                            2 -> "ВТ"
                            3 -> "СР"
                            4 -> "ЧТ"
                            5 -> "ПТ"
                            6 -> "СБ"
                            7 -> "ВС"
                            else -> ""
                        }
                        val isSelected = isSameDay(date, state.selectedDate)

                        // для индикации уроков, т.к. метод suspend вызываем LaunchedEffect
                        var hasLessons by remember { mutableStateOf(false) }
                        LaunchedEffect(date) {
                            hasLessons = viewModel.checkIfDateHasLessons(date)
                        }

                        // Определяем цвет для выходных
                        val isWeekend =
                            localDate.dayOfWeek.value == 6 || localDate.dayOfWeek.value == 7

                        // Цвет для текста дня недели
                        val dayOfWeekColor = when {
                            isWeekend && !isSelected -> WeekendsText
                            isWeekend && isSelected -> WeekendsText
                            else -> WeekdaysText
                        }

                        // Цвет для текста числа
                        val numberTextColor = when {
                            isSelected -> White // Белый для выделенной даты
                            isWeekend -> WeekendsText // для невыделенных выходных
                            else -> WeekdaysText // для будних дней
                        }

                        // Фон для числа
                        val numberBackgroundColor = if (isSelected) BlueSelected else Transparent


                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .width(dayWidth)
                                .pointerInput(Unit) {
                                    detectTapGestures(onTap = {
                                        viewModel.processCommand(ScheduleCommands.SelectDate(date))
                                    })
                                }
                        ) {
                            // День недели
                            Text(
                                text = dayOfWeek,
                                style = MaterialTheme.typography.bodyLarge,
                                color = dayOfWeekColor, // Цвет зависит от выходного или буднего дня
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )

                            // Число
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(numberBackgroundColor),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = dayNumber,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = numberTextColor,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }

                            // Индикатор наличия занятий
                            if (!isSelected) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .size(4.dp)
                                        .clip(CircleShape)
                                        .background(if (hasLessons) Color(0xFF2196F3) else Color.Transparent)
                                )
                            } else {
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }
                    }
                }

                // Разделительная линия
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFFE0E0E0))
                )

                // Контентная часть
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    // Количество занятий
                    if (state.lessonsCount > 0 && state.lastLessonEndTime != null) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${getLessonsCountText(state.lessonsCount)} до ${state.lastLessonEndTime}",
                                style = MaterialTheme.typography.titleMedium,
                                color = SummaryTextColor,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .width(370.dp)
                                    .height(24.dp)
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.height(44.dp))
                    }

                    // Список занятий
                    if (state.isEmptyState) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                // Отступ сверху 112px до иконки
                                Spacer(modifier = Modifier.height(112.dp))

                                // Круглая иконка с солнцем
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(CircleShape)
                                        .background(SunIconBackground.copy(alpha = 0.15f))
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_sun),
                                        contentDescription = null,
                                        tint = SunIconBackground,
                                        modifier = Modifier
                                            .size(33.dp)
                                            .align(Alignment.Center)
                                    )
                                }

                                // Отступ от иконки до текста Выходной 16px
                                Spacer(modifier = Modifier.height(16.dp))

                                // Текст Выходной
                                Text(
                                    text = "Выходной",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Black,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(28.dp)
                                        .padding(horizontal = 50.dp),
                                    textAlign = TextAlign.Center
                                )

                                // Отступ от Выходной до описания 8px
                                Spacer(modifier = Modifier.height(8.dp))

                                // Текст описания
                                Text(
                                    text = "В этот день нет занятий. Отдыхайте и набирайтесь сил :)",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = SummaryTextColor,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .padding(horizontal = 50.dp),
                                    textAlign = TextAlign.Center,
                                    lineHeight = 24.sp
                                )

                                // Отступ снизу 330px
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }

                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            itemsIndexed(state.lessons) { index, lesson ->
                                LessonCard(
                                    lesson = lesson,
                                    onClick = {
                                        // Переход к деталям урока
                                        onLessonClick(lesson.id)
                                    }
                                )

                                if (index < state.lessons.size - 1) {
                                    val nextLesson = state.lessons[index + 1]
                                    val breakDuration = lesson.getBreakDuration()

                                    if (breakDuration != null) {

                                        Spacer(modifier = Modifier.height(4.dp))

                                        BreakIndicator(
                                            startTime = lesson.endTime,
                                            endTime = nextLesson.startTime,
                                            duration = breakDuration
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))
                                    }
                                }
                            }
                            item {
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }
                }
                funDialog()
            }
        }
    }
}

// Компонент Home Tab - РАСПИСАНИЕ
@Composable
fun HomeTab(
    modifier: Modifier = Modifier,
    isActive: Boolean = true,
    onHomeClick: () -> Unit
) {
    val iconColor = if (isActive) BlueToday else Muted
    val textColor = if (isActive) BlueToday else Muted

    Box(
        modifier = modifier
            .width(146.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { onHomeClick() })
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(top = 8.dp, bottom = 8.dp)
        ) {
            // Calendar Icon
            Box(
                modifier = Modifier
                    .size(24.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_calendar),
                    contentDescription = "Расписание",
                    tint = iconColor,
                    modifier = Modifier
                        .size(21.dp)
                        .align(Alignment.Center)
                        .offset(x = 1.5.dp, y = 1.5.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Text Label
            Text(
                text = "Расписание",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Medium,
                color = textColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

// Компонент Center Tab - QR
@Composable
fun CenterTab(
    modifier: Modifier = Modifier,
    onQrCodeClick: () -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter // Выравнивание по верхнему краю
    ) {
        // Круглая кнопка сканирования
        Box(
            modifier = Modifier
                .size(52.dp)
                .offset(y = (-9).dp) // Поднимаем вверх, чтобы выступала
                .clip(CircleShape)
                .background(ScanBackground)
                .border(4.dp, LightBg, CircleShape)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { onQrCodeClick() })
                },
            contentAlignment = Alignment.Center
        ) {
            // Иконка сканирования
            Icon(
                painter = painterResource(id = R.drawable.ic_scan),
                contentDescription = "Сканировать",
                tint = BlueScan,
                modifier = Modifier.size(23.4.dp)
            )
        }
    }
}

// Компонент Profile Tab - ПРОФИЛЬ
@Composable
fun ProfileTab(
    modifier: Modifier = Modifier,
    isActive: Boolean = false,
    onProfileClick: () -> Unit
) {
    val iconColor = if (isActive) BlueToday else Muted
    val textColor = if (isActive) BlueToday else Muted

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(onTap = { onProfileClick() })
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(top = 8.dp, bottom = 8.dp)
        ) {
            // Profile Icon
            Box(
                modifier = Modifier
                    .size(24.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_profile),
                    contentDescription = "Профиль",
                    tint = iconColor,
                    modifier = Modifier
                        .size(19.5.dp, 18.75.dp)
                        .align(Alignment.TopStart)
                        .offset(x = 2.25.dp, y = 2.25.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Text Label
            Text(
                text = "Мой профиль",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Medium,
                color = textColor,
                modifier = Modifier
                    .width(74.dp)
                    .height(16.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

// Форматирование месяца и года
fun formatMonthYear(date: Instant): String {
    val localDate = date.atZone(ZoneId.systemDefault()).toLocalDate()
    val formatter = DateTimeFormatter.ofPattern("LLLL yyyy", Locale("ru"))
    return localDate.format(formatter).replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
    }
}

@Composable
fun LessonCard(
    lesson: Lesson,
    onClick: () -> Unit = {}
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .width(358.dp)
            .padding(horizontal = 16.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { onClick() })
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = LessonCardColor,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(
                    top = 12.dp,
                    bottom = 16.dp,
                    start = 16.dp,
                    end = 16.dp
                )
        ) {
            // Верхняя строка с Типом занятия и иконкой перехода
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Левая часть - теги
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Тег с номером пары
                    TagNumber(number = lesson.pairNumber.toString())

                    // Тег с типом занятия (лекция/семинар/зачет/экзамен)
                    when (lesson.lessonType) {
                        LessonType.LECTURE,
                        LessonType.SEMINAR,
                        LessonType.EXAM,
                        LessonType.TEST -> {
                            TagLessonType(
                                type = lesson.lessonTypeDisplay,
                                lessonType = lesson.lessonType
                            )
                        }

                        else -> {} // Другие типы не показываем
                    }

                    // Тег со статусом (замена/отмена)
                    when (lesson.status) {
                        LessonStatus.SUBSTITUTION -> {
                            TagSubstitution()
                        }

                        LessonStatus.CANCELLED -> {
                            TagCancelled()
                        }

                        else -> {} // NORMAL - ничего не показываем
                    }
                }

                // Правая часть - иконка перехода
                Box(
                    modifier = Modifier
                        .size(width = 8.dp, height = 22.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_chevron_right),
                        contentDescription = "Подробнее",
                        tint = DrillInColor,
                        modifier = Modifier
                            .size(8.dp, 22.dp)
                            .align(Alignment.Center)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Время и аудитория
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = lesson.formattedTime,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TimeLessonTextColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.width(88.dp)
                )

                Text(
                    text = lesson.audienceDisplay,
                    style = MaterialTheme.typography.bodyMedium,
                    color = RoomNumberTextColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.width(58.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Название дисциплины
            Text(
                text = lesson.discipline,
                style = MaterialTheme.typography.bodyLarge,
                color = SubjectText,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Преподаватель
            val hasOriginalTeacher = !lesson.originalTeacher.isNullOrBlank()
            val hasSubstitutionTeacher = !lesson.substitutionTeacher.isNullOrBlank()

            if (hasOriginalTeacher || hasSubstitutionTeacher) {
                val teacherToShow = when {
                    // Для замены показываем заменяющего преподавателя
                    lesson.status == LessonStatus.SUBSTITUTION && hasSubstitutionTeacher ->
                        lesson.substitutionTeacher

                    // Для всех остальных случаев показываем оригинального преподавателя
                    hasOriginalTeacher -> lesson.originalTeacher

                    // Если нет ни того, ни другого (не должно происходить)
                    else -> ""
                }

                Text(
                    text = teacherToShow,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TeacherTextColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                )
            }
        }
    }
}

// Компонент для тега с номером
@Composable
fun TagNumber(number: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(1000.dp))
            .background(TagNumberBackground)
            .padding(
                top = 3.dp,
                bottom = 3.dp,
                start = 11.dp,
                end = 11.dp
            )
    ) {
        Text(
            text = number,
            style = MaterialTheme.typography.labelSmall,
            color = SubjectText,
            modifier = Modifier
                .height(16.dp)
                .align(Alignment.Center)
        )
    }
}

// Компонент для тега с типом занятия
@Composable
fun TagLessonType(
    type: String,
    lessonType: LessonType
) {
    val backgroundColor = when (lessonType) {
        LessonType.LECTURE -> TagLessonTypeLecture
        LessonType.SEMINAR -> TagLessonTypeSeminar
        LessonType.EXAM -> TagLessonTypeExam
        LessonType.TEST -> TagLessonTypeTest
        else -> Color.Gray
    }

    val displayText = when (lessonType) {
        LessonType.LECTURE -> "лекция"
        LessonType.SEMINAR -> "семинар"
        LessonType.EXAM -> "экзамен"
        LessonType.TEST -> "зачет"
        else -> ""
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(1000.dp))
            .background(backgroundColor)
            .padding(
                top = 3.dp,
                bottom = 3.dp,
                start = 11.dp,
                end = 11.dp
            )
    ) {
        Text(
            text = displayText,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White,
            modifier = Modifier
                .height(16.dp)
                .align(Alignment.Center)
        )
    }
}

// Компонент для тега замена
@Composable
fun TagSubstitution(originalTeacher: String? = null) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(1000.dp))
            .background(TagStatusSubstitution)
            .padding(
                top = 3.dp,
                bottom = 3.dp,
                start = 11.dp,
                end = 11.dp
            )
    ) {
        Text(
            text = "замена",
            style = MaterialTheme.typography.labelSmall,
            color = Color.White,
            modifier = Modifier
                .height(16.dp)
                .align(Alignment.Center)
        )
    }
}

// Компонент для тега отменено
@Composable
fun TagCancelled() {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(1000.dp))
            .background(TagLessonCancel)
            .padding(
                top = 3.dp,
                bottom = 3.dp,
                start = 11.dp,
                end = 11.dp
            )
    ) {
        Text(
            text = "отменено",
            style = MaterialTheme.typography.labelSmall,
            color = Color.White,
            modifier = Modifier
                .height(16.dp)
                .align(Alignment.Center)
        )
    }
}

@Composable
fun BreakIndicator(
    startTime: String,
    endTime: String,
    duration: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(22.dp)
            .padding(horizontal = 16.dp)
            .padding(
                top = 3.dp,
                bottom = 3.dp
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // текст - "перерыв X минут"
        Text(
            text = "перерыв $duration",
            style = MaterialTheme.typography.labelSmall,
            color = BreakTextColor,
            modifier = Modifier.height(16.dp)
        )

        // текст - интервал времени "11:45 – 12:00"
        Text(
            text = "$startTime – $endTime",
            style = MaterialTheme.typography.labelSmall,
            color = BreakTextColor,
            modifier = Modifier.height(16.dp),
            textAlign = TextAlign.End
        )
    }
}

// Компонент для пункта нижней навигации
@Composable
fun BottomNavItem(
    modifier: Modifier = Modifier,
    isActive: Boolean,
    icon: Int,
    label: String,
    onClick: () -> Unit
) {
    val iconTint = if (isActive) Color(0xFF3B82F6) else Color(0xFF9E9E9E)
    val textColor = if (isActive) Color(0xFF3B82F6) else Color(0xFF9E9E9E)

    val iconBackgroundColor = if (isActive) {
        Color(0xFF3B82F6).copy(alpha = 0.12f)
    } else {
        Color.Transparent
    }

    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier
            .fillMaxHeight()
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .width(56.dp)
                .height(32.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(iconBackgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = label,
                modifier = Modifier.size(24.dp),
                tint = iconTint
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 10.sp,
            color = textColor,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Visible
        )
    }
}

// Функция для склонения слова "занятия" в зависимости от количества уроков
fun getLessonsCountText(count: Int): String {
    return when {
        count % 10 == 1 && count % 100 != 11 -> "$count занятие"        // 1 занятие, 21 занятие, 101 занятие
        count % 10 in 2..4 && (count % 100 !in 12..14) -> "$count занятия" // 2-4 занятия, 22-24 занятия
        else -> "$count занятий"                                          // 0, 5-20, 25-30 занятий
    }
}