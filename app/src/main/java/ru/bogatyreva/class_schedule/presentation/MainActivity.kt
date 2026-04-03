package ru.bogatyreva.class_schedule.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import ru.bogatyreva.class_schedule.data.TestScheduleRepositoryImpl
import ru.bogatyreva.class_schedule.presentation.screens.lesson.LessonDetailsCommands
import ru.bogatyreva.class_schedule.presentation.screens.lesson.LessonDetailsScreen
import ru.bogatyreva.class_schedule.presentation.screens.lesson.LessonDetailsViewModel
import ru.bogatyreva.class_schedule.presentation.screens.schedule.ScheduleScreen
import ru.bogatyreva.class_schedule.presentation.screens.schedule.ScheduleViewModel
import ru.bogatyreva.class_schedule.presentation.ui.theme.Class_ScheduleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = TestScheduleRepositoryImpl()

        // Создаём ViewModel ОДИН раз (как и ScheduleViewModel)
        val scheduleViewModel = ScheduleViewModel(repository)
        val lessonDetailsViewModel = LessonDetailsViewModel(repository)  // одна ViewModel

        setContent {
            Class_ScheduleTheme {

                var currentScreen by remember { mutableStateOf<Screen>(Screen.Schedule) }

                when (currentScreen) {
                    Screen.Schedule -> {
                        ScheduleScreen(
                            viewModel = scheduleViewModel,
                            onLessonClick = { lessonId ->
                                // Перед переходом отправляем команду загрузить урок
                                lessonDetailsViewModel.processCommand(LessonDetailsCommands.LoadLesson(lessonId))
                                currentScreen = Screen.LessonDetails
                            },
                            onProfileClick = { /* TODO */ },
                            onQrCodeClick = { /* TODO */ }
                        )
                    }
                    Screen.LessonDetails -> {
                        LessonDetailsScreen(
                            viewModel = lessonDetailsViewModel,
                            onBackPressed = {
                                currentScreen = Screen.Schedule
                            },
                            onQrCodeClick = {
                                // TODO: Открыть QR-сканер
                            },
                            onProfileClick = {
                                // TODO: Перейти к профилю
                            }
                        )
                    }
                }
            }
        }
    }
}

sealed class Screen {
    data object Schedule : Screen()
    data object LessonDetails : Screen()
}