package ru.bogatyreva.class_schedule.presentation.screens.lesson

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.bogatyreva.class_schedule.R
import ru.bogatyreva.class_schedule.domain.model.Assignment
import ru.bogatyreva.class_schedule.domain.model.Lesson
import ru.bogatyreva.class_schedule.domain.model.LessonMaterial
import ru.bogatyreva.class_schedule.domain.model.LessonStatus
import ru.bogatyreva.class_schedule.domain.model.LessonType
import ru.bogatyreva.class_schedule.domain.model.SubmittedFile
import ru.bogatyreva.class_schedule.domain.model.SubmittedMaterial
import ru.bogatyreva.class_schedule.presentation.screens.schedule.CenterTab
import ru.bogatyreva.class_schedule.presentation.screens.schedule.HomeTab
import ru.bogatyreva.class_schedule.presentation.screens.schedule.ProfileTab
import ru.bogatyreva.class_schedule.presentation.screens.schedule.TagCancelled
import ru.bogatyreva.class_schedule.presentation.screens.schedule.TagLessonType
import ru.bogatyreva.class_schedule.presentation.screens.schedule.TagSubstitution
import ru.bogatyreva.class_schedule.presentation.ui.theme.ActiveBackgroundColor
import ru.bogatyreva.class_schedule.presentation.ui.theme.ActiveTextColor
import ru.bogatyreva.class_schedule.presentation.ui.theme.AddFileButtonTextStyle
import ru.bogatyreva.class_schedule.presentation.ui.theme.AvatarBackground
import ru.bogatyreva.class_schedule.presentation.ui.theme.BlueToday
import ru.bogatyreva.class_schedule.presentation.ui.theme.DateTextStyle
import ru.bogatyreva.class_schedule.presentation.ui.theme.FileNameTextStyle
import ru.bogatyreva.class_schedule.presentation.ui.theme.FileSizeTextStyle
import ru.bogatyreva.class_schedule.presentation.ui.theme.InactiveBackgroundColor
import ru.bogatyreva.class_schedule.presentation.ui.theme.InactiveTextColor
import ru.bogatyreva.class_schedule.presentation.ui.theme.InitialsTextStyle
import ru.bogatyreva.class_schedule.presentation.ui.theme.LessonCardColor
import ru.bogatyreva.class_schedule.presentation.ui.theme.LessonTimeTextStyle
import ru.bogatyreva.class_schedule.presentation.ui.theme.LightBg
import ru.bogatyreva.class_schedule.presentation.ui.theme.PlaceTitleTextStyle
import ru.bogatyreva.class_schedule.presentation.ui.theme.RoomColorDetailLesson
import ru.bogatyreva.class_schedule.presentation.ui.theme.Separator
import ru.bogatyreva.class_schedule.presentation.ui.theme.SummaryTextColor
import ru.bogatyreva.class_schedule.presentation.ui.theme.TeacherNameTextStyle
import ru.bogatyreva.class_schedule.presentation.ui.theme.TextColorDetailLesson
import ru.bogatyreva.class_schedule.presentation.ui.theme.TitleText
import ru.bogatyreva.class_schedule.presentation.ui.theme.White
import ru.bogatyreva.class_schedule.utils.getInitials
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonDetailsScreen(
    viewModel: LessonDetailsViewModel,
    onBackPressed: () -> Unit = {},
    onQrCodeClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableStateOf(LessonDetailsTab.INFO) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Занятие",
                        style = MaterialTheme.typography.titleLarge,
                        color = TitleText
                    )
                },
                navigationIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Назад",
                        modifier = Modifier
                            .size(24.dp)
                            .padding(start = 4.dp)
                            .clickable { onBackPressed() },
                        tint = TitleText
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                ),
                modifier = Modifier.padding(vertical = 18.dp)
            )
        },
        bottomBar = {
            // Нижняя панель - Navigation Menu (как на главном экране)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(94.dp)
                    .background(LightBg)
                    .border(
                        width = 1.dp,
                        color = Color.Black.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp)
                    )
                    .padding(horizontal = 16.dp)
            ) {
                // Расписание (слева)
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 16.dp, top = 14.dp)
                ) {
                    HomeTab(
                        modifier = Modifier
                            .width(146.dp)
                            .height(56.dp),
                        isActive = false,  // на экране детальной информации неактивно
                        onHomeClick = { }
                    )
                }

                // Профиль (справа)
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 16.dp, top = 14.dp)
                ) {
                    ProfileTab(
                        modifier = Modifier
                            .width(146.dp)
                            .height(56.dp),
                        isActive = false,  // на экране детальной информации неактивно
                        onProfileClick = onProfileClick
                    )
                }

                // Кнопка Scan (по центру)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 18.dp)
                ) {
                    CenterTab(
                        modifier = Modifier
                            .width(66.dp)
                            .height(70.dp),
                        onQrCodeClick = onQrCodeClick
                    )
                }
            }
        },
        containerColor = White
    ) { paddingValues ->

        if (state.isLoading && state.lesson == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(White)
            ) {
                LessonDateHeader(lesson = state.lesson)
                LessonInfoContent(lesson = state.lesson)

                // Сегментированные кнопки
                val activeBackgroundColor = ActiveBackgroundColor
                val activeTextColor = ActiveTextColor
                val inactiveBackgroundColor = InactiveBackgroundColor
                val inactiveTextColor = InactiveTextColor

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 0.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    // Информация
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .clip(
                                RoundedCornerShape(
                                    topStart = 20.dp,
                                    topEnd = 8.dp,
                                    bottomStart = 20.dp,
                                    bottomEnd = 8.dp
                                )
                            )
                            .background(
                                if (selectedTab == LessonDetailsTab.INFO) activeBackgroundColor else inactiveBackgroundColor
                            )
                            .clickable { selectedTab = LessonDetailsTab.INFO },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Информация",
                            style = MaterialTheme.typography.labelLarge.copy(
                                letterSpacing = 0.1.sp
                            ),
                            fontWeight = FontWeight.Medium,
                            color = if (selectedTab == LessonDetailsTab.INFO) activeTextColor else inactiveTextColor,
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .padding(horizontal = 15.5.dp)
                        )
                    }

                    // Материалы
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .clip(
                                if (selectedTab == LessonDetailsTab.MATERIALS) {
                                    RoundedCornerShape(20.dp)
                                } else {
                                    RoundedCornerShape(8.dp)
                                }
                            )
                            .background(
                                if (selectedTab == LessonDetailsTab.MATERIALS) activeBackgroundColor else inactiveBackgroundColor
                            )
                            .clickable { selectedTab = LessonDetailsTab.MATERIALS },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Материалы",
                            style = MaterialTheme.typography.labelLarge.copy(
                                letterSpacing = 0.1.sp
                            ),
                            fontWeight = FontWeight.Medium,
                            color = if (selectedTab == LessonDetailsTab.MATERIALS) activeTextColor else inactiveTextColor,
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .padding(horizontal = 20.5.dp)
                        )
                    }

                    // Задание
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .clip(
                                RoundedCornerShape(
                                    topStart = 8.dp,
                                    topEnd = 20.dp,
                                    bottomStart = 8.dp,
                                    bottomEnd = 20.dp
                                )
                            )
                            .background(
                                if (selectedTab == LessonDetailsTab.ASSIGNMENT) activeBackgroundColor else inactiveBackgroundColor
                            )
                            .clickable { selectedTab = LessonDetailsTab.ASSIGNMENT },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Задание",
                            style = MaterialTheme.typography.labelLarge.copy(
                                letterSpacing = 0.1.sp
                            ),
                            fontWeight = FontWeight.Medium,
                            color = if (selectedTab == LessonDetailsTab.ASSIGNMENT) activeTextColor else inactiveTextColor,
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .padding(horizontal = 31.dp)
                        )
                    }
                }

                // Контент по выбранной вкладке
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    when (selectedTab) {
                        LessonDetailsTab.INFO -> {
                            item { InfoTabContent(lesson = state.lesson, viewModel = viewModel) }
                        }

                        LessonDetailsTab.MATERIALS -> {
                            item {
                                MaterialsTabContent(
                                    lessonMaterials = state.lessonMaterials,
                                    submittedMaterials = state.submittedMaterials,
                                    viewModel = viewModel
                                )
                            }
                        }

                        LessonDetailsTab.ASSIGNMENT -> {
                            item {
                                AssignmentTabContent(
                                    assignment = state.assignment,
                                    isSubmitting = state.isSubmitting,
                                    viewModel = viewModel
                                )
                            }
                        }
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }
}

@Composable
fun LessonDateHeader(lesson: Lesson?) {
    if (lesson == null) return

    val date = lesson.date.atZone(ZoneId.systemDefault()).toLocalDate()
    val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("ru"))
    val formattedDate = date.format(formatter)

    Text(
        text = formattedDate,
        style = DateTextStyle,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 8.dp)
    )
}

@Composable
fun LessonInfoContent(lesson: Lesson?) {
    if (lesson == null) return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp, bottom = 16.dp)
    ) {
        // Название дисциплины
        Text(
            text = lesson.discipline,
            style = MaterialTheme.typography.titleLarge,
            color = TitleText,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Время с иконкой
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_time),
                contentDescription = null,
                tint = BlueToday,
                modifier = Modifier.size(16.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = lesson.formattedTime,
                style = LessonTimeTextStyle,
                modifier = Modifier
                    .height(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Теги: тип занятия + статус (если есть)
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (lesson.lessonType != LessonType.UNKNOWN) {
                TagLessonType(
                    type = lesson.lessonTypeDisplay,
                    lessonType = lesson.lessonType
                )
            }

            when (lesson.status) {
                LessonStatus.SUBSTITUTION -> {
                    TagSubstitution()
                }

                LessonStatus.CANCELLED -> {
                    TagCancelled()
                }

                else -> {}
            }
        }
    }
}


@Composable
fun InfoTabContent(
    lesson: Lesson?,
    viewModel: LessonDetailsViewModel
) {
    if (lesson == null) return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {

        // Карточка преподавателя
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = LessonCardColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Аватар с инициалами (нужно также обновить для замены)
                val teacherNameForAvatar = when {
                    lesson.status == LessonStatus.SUBSTITUTION && !lesson.substitutionTeacher.isNullOrBlank() ->
                        lesson.substitutionTeacher
                    else -> lesson.originalTeacher
                }

                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(AvatarBackground),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = getInitials(teacherNameForAvatar),
                        style = InitialsTextStyle,
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Преподаватель",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = TextColorDetailLesson,
                        modifier = Modifier
                            .width(264.dp)
                            .height(18.dp)
                    )

                    // Логика отображения имени преподавателя при замене
                    val hasOriginalTeacher = !lesson.originalTeacher.isNullOrBlank()
                    val hasSubstitutionTeacher = !lesson.substitutionTeacher.isNullOrBlank()

                    val teacherName = when {
                        lesson.status == LessonStatus.SUBSTITUTION && hasSubstitutionTeacher ->
                            lesson.substitutionTeacher
                        hasOriginalTeacher -> lesson.originalTeacher
                        else -> ""
                    }

                    Text(
                        text = teacherName,
                        style = TeacherNameTextStyle,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .width(264.dp)
                            .height(48.dp)
                    )
                }
            }
        }

        // Карточка аудитории / Онлайн
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = LessonCardColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                if (lesson.isOnline) {
                    // Строка: иконка + колонка с текстами + кнопка
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Иконка
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(BlueToday.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_camera),
                                contentDescription = "Видеозвонок",
                                tint = BlueToday,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        // Колонка с текстами
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            // Название формата - "Онлайн"
                            Text(
                                text = "Онлайн",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 0.15.sp,
                                    color = Color(0xFF49454F)
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .width(162.dp)
                                    .height(18.dp)
                            )

                            // Название платформы "Google Meet"
                            Text(
                                text = if (lesson.onlineLink != null) "Google Meet" else "Ссылка отсутствует",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 0.15.sp,
                                    color = TitleText  // #1D1B20
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .width(101.dp)
                                    .height(24.dp)
                            )
                        }

                        // Кнопка "Перейти" (только если есть ссылка)
                        if (!lesson.onlineLink.isNullOrBlank()) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(100.dp))
                                    .background(BlueToday)
                                    .clickable {
                                        viewModel.processCommand(LessonDetailsCommands.OpenOnlineLesson)
                                    }
                                    .padding(vertical = 10.dp, horizontal = 16.dp)
                            ) {
                                Text(
                                    text = "Перейти",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.Medium
                                    ),
                                    color = Color.White,
                                    modifier = Modifier
                                        .width(60.dp)
                                        .height(20.dp)
                                )
                            }
                        }
                    }

                } else {
                    // Офлайн: иконка + колонка с текстом
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(BlueToday.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_location),
                                contentDescription = "Аудитория",
                                tint = BlueToday,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Место проведения",
                                style = PlaceTitleTextStyle,
                                modifier = Modifier
                                    .width(264.dp)
                                    .height(18.dp)
                            )

                            Text(
                                text = "ауд. ${lesson.audience}",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = RoomColorDetailLesson
                                ),
                                modifier = Modifier
                                    .width(93.dp)
                                    .height(28.dp)
                            )

                            Text(
                                text = "Корпус 1, этаж 2",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = TextColorDetailLesson
                                ),
                                modifier = Modifier
                                    .width(118.dp)
                                    .height(20.dp)
                            )
                        }
                    }
                }
            }
        }

        // Тема урока
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, top = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = LessonCardColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Заголовок "Тема урока"
                Text(
                    text = "Тема урока",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        letterSpacing = 0.25.sp
                    ),
                    fontWeight = FontWeight.Medium,
                    color = TextColorDetailLesson,
                    modifier = Modifier
                        .width(326.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Разделительная линия
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Separator)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Название темы
                Text(
                    text = "История права",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp
                    ),
                    color = TitleText,
                    modifier = Modifier
                        .width(326.dp)
                        .height(24.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Описание темы
                Text(
                    text = "На уроке поговорим о нормах международного права и вскользь вспомним прошлую тему. Так же мы посмотрим материалы в которых рассмотрим исторические изменения в международном праве",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        letterSpacing = 0.5.sp
                    ),
                    fontWeight = FontWeight.Normal,
                    color = SummaryTextColor,
                    lineHeight = 24.sp,
                    modifier = Modifier
                        .width(326.dp)
                        .height(144.dp)
                )
            }
        }
    }
}

@Composable
fun MaterialsTabContent(
    lessonMaterials: List<LessonMaterial>,
    submittedMaterials: List<SubmittedMaterial>,  // не используется
    viewModel: LessonDetailsViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        // Состояние 1: Есть материалы к занятию
        if (lessonMaterials.isNotEmpty()) {
            lessonMaterials.forEachIndexed { index, material ->

                if (index == 0) {
                    Spacer(modifier = Modifier.height(16.dp))  // 16px сверху первого материала
                }

                MaterialItem(
                    material = material,
                    onClick = {
                        viewModel.processCommand(
                            LessonDetailsCommands.DownloadMaterial(
                                material
                            )
                        )
                    }
                )

                if (index < lessonMaterials.size - 1) {
                    Spacer(modifier = Modifier.height(8.dp))  // 8px между материалами
                }
            }

            Spacer(modifier = Modifier.height(16.dp))  // 16px снизу последнего материала
        }

        // Состояние 2: Нет материалов к занятию
        if (lessonMaterials.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 56.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_attach),
                    contentDescription = "Нет материалов",
                    tint = BlueToday,
                    modifier = Modifier.size(48.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Материалы еще не добавлены",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = TitleText,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Загляните позже - они появятся здесь",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Normal,
                    color = SummaryTextColor,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun MaterialItem(
    material: LessonMaterial,
    onClick: () -> Unit
) {
    // Определяем цвет фона для типа файла
    val fileTypeBackground = when (material.fileType.uppercase()) {
        "JPG", "JPEG" -> Color(0xFFEFECD2)
        "PDF" -> Color(0xFFEFECD2)
        "PPTX", "PPT" -> Color(0xFFEFECD2)
        "DOCX", "DOC" -> Color(0xFFEFECD2)
        "LINK" -> Color(0xFFDEF6E4)
        else -> LessonCardColor
    }

    val fileTypeBorder = when (material.fileType.uppercase()) {
        "JPG", "JPEG" -> Color(0xFFCFBA19).copy(alpha = 0.3f)
        "LINK" -> Color(0xFF34C759).copy(alpha = 0.3f)
        else -> Color.Transparent
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = LessonCardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Плашка с типом файла/ссылки
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(fileTypeBackground)
                    .border(
                        width = 1.dp,
                        color = fileTypeBorder,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (material.fileType.uppercase() == "LINK") {
                    // Иконка для ссылки
                    Icon(
                        painter = painterResource(id = R.drawable.ic_link),
                        contentDescription = "Ссылка",
                        tint = Color(0xFF34C759),
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    // Текст для файлов
                    Text(
                        text = material.fileType.uppercase(),
                        style = MaterialTheme.typography.labelLarge.copy(
                            letterSpacing = 0.1.sp
                        ),
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF6B5E26),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .width(28.dp)
                            .height(20.dp)
                    )
                }
            }

            // Информация о файле/ссылке (название + размер)
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = material.fileName,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        letterSpacing = 0.5.sp
                    ),
                    fontWeight = FontWeight.Medium,
                    color = TitleText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .width(242.dp)
                        .height(22.dp)
                )

                // Для ссылки размер не показываем
                if (material.fileType.uppercase() != "LINK") {
                    Text(
                        text = material.fileSize,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            letterSpacing = 0.25.sp
                        ),
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF79747E),
                        modifier = Modifier
                            .width(242.dp)
                            .height(20.dp)
                    )
                }
            }

            // Контейнер иконки загрузки — показываем только НЕ для ссылки
            if (material.fileType.uppercase() != "LINK") {
                Box(
                    modifier = Modifier.size(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_download),
                        contentDescription = "Скачать",
                        tint = Color(0xFF79747E),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AssignmentTabContent(
    assignment: Assignment?,
    isSubmitting: Boolean,
    viewModel: LessonDetailsViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        if (assignment == null) {
            Text(
                text = "Задание не задано",
                style = MaterialTheme.typography.bodyMedium,
                color = SummaryTextColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                textAlign = TextAlign.Center
            )
            return
        }

        // Контейнер с описанием задания "Что нужно сделать"
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = LessonCardColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Что нужно сделать:",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        letterSpacing = 0.5.sp
                    ),
                    fontWeight = FontWeight.SemiBold,
                    color = TitleText,
                    modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                        .padding(bottom = 12.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(1.dp)
                        .background(Color(0xFFE6E6E6))
                )

                Text(
                    text = assignment.description,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        letterSpacing = 0.5.sp
                    ),
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF1D1B20),
                    modifier = Modifier
                        .padding(top = 12.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Заголовок "Сдать задание"
        Text(
            text = "Сдать задание",
            style = MaterialTheme.typography.bodyLarge.copy(
                letterSpacing = 0.5.sp
            ),
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1D1B20),
            modifier = Modifier
                .padding(bottom = 12.dp)
        )

        // Если есть файлы — показываем зелёный контейнер
        if (assignment.submittedFiles.isNotEmpty()) {
            // Зелёный контейнер с файлами
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF3ABF52).copy(alpha = 0.06f))
                    .border(
                        width = 1.5.dp,
                        color = Color(0xFF3ABF52).copy(alpha = 0.4f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    assignment.submittedFiles.forEach { file ->
                        SubmittedFileItem(
                            fileName = file.fileName,
                            fileSize = file.fileSize,
                            fileType = file.fileType,
                            onDelete = {
                                viewModel.processCommand(LessonDetailsCommands.RemoveFile(file.id))
                            }
                        )
                    }

                    if (assignment.submittedFiles.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Color(0xFF3ABF52).copy(alpha = 0.5f))
                        )
                    }

                    // Кнопка "Добавить файл" (АКТИВНАЯ)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val newFile = SubmittedFile(
                                    id = "file_${System.currentTimeMillis()}",
                                    fileName = "новый_файл.pdf",
                                    fileSize = "1.5 MB",
                                    fileType = "PDF"
                                )
                                viewModel.processCommand(LessonDetailsCommands.AddFile(newFile))
                            }
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Добавить файл",
                            style = AddFileButtonTextStyle,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопка: активная "Сдать задание" или неактивная "Задание сдано"
            if (!assignment.submitted) {
                // Активная кнопка "Сдать задание"
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(100.dp))
                        .background(BlueToday)
                        .clickable {
                            viewModel.processCommand(LessonDetailsCommands.SubmitAssignment(""))
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Сдать задание",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            } else {
                // Неактивная кнопка "Задание сдано"
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(100.dp))
                        .background(Color(0xFFDEE5E0).copy(alpha = 0.38f))
                        .clickable(enabled = false) { },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(
                            horizontal = 24.dp,
                            vertical = 16.dp
                        )
                    ) {
                        // Иконка
                        Icon(
                            painter = painterResource(id = R.drawable.ic_hometask_checked),
                            contentDescription = "Задание сдано",
                            tint = Color(0xFF1D1B20),
                            modifier = Modifier.size(16.3.dp, 12.03.dp)
                        )

                        // Текст "Задание сдано"
                        Text(
                            text = "Задание сдано",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF1D1B20),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        } else {
            // Нет файлов — показываем поле загрузки
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(144.dp)
                    .drawWithContent {
                        drawContent()
                        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)
                        drawRoundRect(
                            color = Color.Black.copy(alpha = 0.2f),
                            style = Stroke(
                                width = 1.5f,
                                pathEffect = pathEffect
                            ),
                            cornerRadius = CornerRadius(16f, 16f)
                        )
                    }
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .clickable {
                        val newFile = SubmittedFile(
                            id = "file_${System.currentTimeMillis()}",
                            fileName = "новый_файл.pdf",
                            fileSize = "1.5 MB",
                            fileType = "PDF"
                        )
                        viewModel.processCommand(LessonDetailsCommands.AddFile(newFile))
                    }
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(47.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFD6ECFF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_task_download),
                            contentDescription = "Загрузить",
                            tint = BlueToday,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Прикрепить файл",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            letterSpacing = 0.5.sp
                        ),
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1D1B20),
                        modifier = Modifier
                            .height(24.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "PDF, DOCX, JPG — до 20 МБ",
                        style = MaterialTheme.typography.labelMedium.copy(
                            letterSpacing = 0.4.sp
                        ),
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF79747E),
                        modifier = Modifier
                            .height(16.dp)
                    )
                }
            }
        }

        if (isSubmitting) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

// Компонент для загруженного файла (с иконкой удаления)
@Composable
fun SubmittedFileItem(
    fileName: String,
    fileSize: String,
    fileType: String,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Плашка с типом файла
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFDEF6E4))
                .border(
                    width = 1.dp,
                    color = Color(0xFF34C759).copy(alpha = 0.3f),
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = fileType.uppercase(),
                style = MaterialTheme.typography.labelLarge.copy(
                    letterSpacing = 0.1.sp
                ),
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = Color(0xFF34C759),
                textAlign = TextAlign.Center
            )
        }

        // Информация о файле
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Название файла
            Text(
                text = fileName,
                style = FileNameTextStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.width(242.dp)
            )

            // Размер файла
            Text(
                text = fileSize,
                style = FileSizeTextStyle,
                modifier = Modifier.width(242.dp)
            )
        }

        // Контейнер иконки удаления
        Box(
            modifier = Modifier
                .size(24.dp)
                .clickable { onDelete() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_delete),
                contentDescription = "Удалить",
                tint = Color(0xFFFF5F57),
                modifier = Modifier.size(16.dp, 18.dp)
            )
        }
    }
}