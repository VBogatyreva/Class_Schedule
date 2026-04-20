package ru.bogatyreva.class_schedule.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.bogatyreva.class_schedule.data.AuthRepositoryImpl
import ru.bogatyreva.class_schedule.data.TestScheduleRepositoryImpl
import ru.bogatyreva.class_schedule.domain.usecase.LoginUseCase
import ru.bogatyreva.class_schedule.domain.usecase.LogoutUseCase
import ru.bogatyreva.class_schedule.presentation.screens.auth.AuthViewModel
import ru.bogatyreva.class_schedule.presentation.screens.auth.HowToGetAccountScreen
import ru.bogatyreva.class_schedule.presentation.screens.auth.LoginScreen
import ru.bogatyreva.class_schedule.presentation.screens.auth.ResetPasswordScreen
import ru.bogatyreva.class_schedule.presentation.screens.auth.WelcomeScreen
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
        val authRepository = AuthRepositoryImpl()

        // UseCases для авторизации
        val loginUseCase = LoginUseCase(authRepository)
        val logoutUseCase = LogoutUseCase(authRepository)

        // ViewModels
        val scheduleViewModel = ScheduleViewModel(repository)
        val lessonDetailsViewModel = LessonDetailsViewModel(repository)
        val authViewModel = AuthViewModel(loginUseCase, logoutUseCase)

        setContent {
            Class_ScheduleTheme {
                AppNavigation(
                    scheduleViewModel = scheduleViewModel,
                    lessonDetailsViewModel = lessonDetailsViewModel,
                    authViewModel = authViewModel
                )
            }
        }
    }
}

@Composable
fun AppNavigation(
    scheduleViewModel: ScheduleViewModel,
    lessonDetailsViewModel: LessonDetailsViewModel,
    authViewModel: AuthViewModel
) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Welcome) }
    val authState by authViewModel.state.collectAsStateWithLifecycle()

    // Если пользователь авторизован, показываем основное приложение
    androidx.compose.runtime.LaunchedEffect(authState.isAuthenticated) {
        if (authState.isAuthenticated) {
            currentScreen = Screen.Schedule
        }
    }

    when (currentScreen) {
        Screen.Welcome -> {
            WelcomeScreen(
                onLoginClick = { currentScreen = Screen.Login },
                onHowToGetAccountClick = { currentScreen = Screen.HowToGetAccount }
            )
        }

        Screen.Login -> {
            LoginScreen(
                viewModel = authViewModel,
                onBackClick = { currentScreen = Screen.Welcome },
                onForgotPasswordClick = { currentScreen = Screen.ResetPassword },
                onLoginSuccess = { /* Переход произойдет автоматически через LaunchedEffect */ }
            )
        }

        Screen.ResetPassword -> {
            ResetPasswordScreen(
                onBackClick = { currentScreen = Screen.Login }
            )
        }

        Screen.HowToGetAccount -> {
            HowToGetAccountScreen(
                onBackClick = { currentScreen = Screen.Welcome }
            )
        }

        Screen.Schedule -> {
            ScheduleScreen(
                viewModel = scheduleViewModel,
                onLessonClick = { lessonId ->
                    lessonDetailsViewModel.processCommand(LessonDetailsCommands.LoadLesson(lessonId))
                    currentScreen = Screen.LessonDetails
                },
                onProfileClick = {
                    // Выход из аккаунта
                    authViewModel.processCommand(ru.bogatyreva.class_schedule.presentation.screens.auth.AuthCommands.Logout)
                    currentScreen = Screen.Welcome
                },
                onQrCodeClick = { /* TODO: Открыть QR-сканер */ }
            )
        }

        Screen.LessonDetails -> {
            LessonDetailsScreen(
                viewModel = lessonDetailsViewModel,
                onBackPressed = { currentScreen = Screen.Schedule },
                onQrCodeClick = { /* TODO: Открыть QR-сканер */ },
                onProfileClick = {
                    authViewModel.processCommand(ru.bogatyreva.class_schedule.presentation.screens.auth.AuthCommands.Logout)
                    currentScreen = Screen.Welcome
                }
            )
        }
    }
}

sealed class Screen {
    data object Welcome : Screen()
    data object Login : Screen()
    data object ResetPassword : Screen()
    data object HowToGetAccount : Screen()
    data object Schedule : Screen()
    data object LessonDetails : Screen()
}