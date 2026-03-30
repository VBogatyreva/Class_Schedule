package ru.bogatyreva.class_schedule.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ru.bogatyreva.class_schedule.data.TestScheduleRepositoryImpl
import ru.bogatyreva.class_schedule.presentation.screens.ScheduleScreen
import ru.bogatyreva.class_schedule.presentation.screens.ScheduleViewModel
import ru.bogatyreva.class_schedule.presentation.ui.theme.Class_ScheduleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = TestScheduleRepositoryImpl()
        val viewModel = ScheduleViewModel(repository)

        setContent {
            Class_ScheduleTheme {
                ScheduleScreen(viewModel)
            }
        }
    }
}