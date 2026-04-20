package ru.bogatyreva.class_schedule.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import ru.bogatyreva.class_schedule.presentation.screens.lesson.LessonDetailsViewModel
import ru.bogatyreva.class_schedule.presentation.screens.schedule.ScheduleViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            // Создаём ViewModel ОДИН раз (как и ScheduleViewModel)
            val lessonViewModel: LessonDetailsViewModel = hiltViewModel()
            val scheduleScreenViewModel: ScheduleViewModel = hiltViewModel()

            NavigationHost(
                lessonViewModel = lessonViewModel,
                scheduleScreenViewModel = scheduleScreenViewModel
            )

        }
    }
}