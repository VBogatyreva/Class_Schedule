package ru.bogatyreva.class_schedule.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.delay
import ru.bogatyreva.class_schedule.presentation.screens.auth.AuthCommands
import ru.bogatyreva.class_schedule.presentation.screens.auth.AuthViewModel
import ru.bogatyreva.class_schedule.presentation.screens.auth.HowToGetAccountScreen
import ru.bogatyreva.class_schedule.presentation.screens.auth.LoginScreen
import ru.bogatyreva.class_schedule.presentation.screens.auth.ResetPasswordScreen
import ru.bogatyreva.class_schedule.presentation.screens.auth.WelcomeScreen
import ru.bogatyreva.class_schedule.presentation.screens.career.CareerScreen
import ru.bogatyreva.class_schedule.presentation.screens.career.CareerViewModel
import ru.bogatyreva.class_schedule.presentation.screens.lesson.LessonDetailsCommands
import ru.bogatyreva.class_schedule.presentation.screens.lesson.LessonDetailsScreen
import ru.bogatyreva.class_schedule.presentation.screens.lesson.LessonDetailsViewModel
import ru.bogatyreva.class_schedule.presentation.screens.profile.ProfileScreen
import ru.bogatyreva.class_schedule.presentation.screens.qrscan.QrScanView
import ru.bogatyreva.class_schedule.presentation.screens.schedule.ErrorMarkDialog
import ru.bogatyreva.class_schedule.presentation.screens.schedule.MarkDialog
import ru.bogatyreva.class_schedule.presentation.screens.schedule.MarkDialogState
import ru.bogatyreva.class_schedule.presentation.screens.schedule.MarkView
import ru.bogatyreva.class_schedule.presentation.screens.schedule.ScheduleCommands
import ru.bogatyreva.class_schedule.presentation.screens.schedule.ScheduleScreen
import ru.bogatyreva.class_schedule.presentation.screens.schedule.ScheduleViewModel
import ru.bogatyreva.class_schedule.presentation.screens.vacancies.VacancyDetailsScreen
import ru.bogatyreva.class_schedule.presentation.screens.vacancies.VacancyDetailsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationHost(
    lessonViewModel: LessonDetailsViewModel = hiltViewModel(),
    scheduleScreenViewModel: ScheduleViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    careerViewModel: CareerViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    var dialogState by remember { mutableStateOf<MarkDialogState>(MarkDialogState.DEFAULT) }

    val authState by authViewModel.state.collectAsStateWithLifecycle()

    var hasCameraPermission = remember {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            hasCameraPermission = isGranted
        }
    )



    NavHost(
        navController = navController,
        startDestination = if (authState.isAuthenticated) {
            Screens.SCHEDULE.name
        } else {
            Screens.WELCOME.name
        }
    ) {
        // Экран приветствия
        composable(route = Screens.WELCOME.name) {
            WelcomeScreen(
                onLoginClick = { navController.navigate(Screens.LOGIN.name) },
                onHowToGetAccountClick = { navController.navigate(Screens.HOW_TO_GET_ACCOUNT.name) }
            )
        }
        // Экран входа
        composable(route = Screens.LOGIN.name) {
            LoginScreen(
                viewModel = authViewModel,
                onBackClick = { navController.popBackStack() },
                onForgotPasswordClick = { navController.navigate(Screens.RESET_PASSWORD.name) },
                onLoginSuccess = {
                    navController.popBackStack(Screens.WELCOME.name, inclusive = true)
                    navController.navigate(Screens.SCHEDULE.name)
                }
            )
        }

        // Экран "Как получить аккаунт"
        composable(route = Screens.HOW_TO_GET_ACCOUNT.name) {
            HowToGetAccountScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // Экран сброса пароля
        composable(route = Screens.RESET_PASSWORD.name) {
            ResetPasswordScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // Экран расписания
        composable(route = Screens.SCHEDULE.name) {
            ScheduleScreen(
                viewModel = scheduleScreenViewModel,
                onLessonClick = { lessonId ->
                    // Перед переходом отправляем команду загрузить урок
                    lessonViewModel.processCommand(LessonDetailsCommands.LoadLesson(lessonId))
                    navController.navigate(Screens.LESSON.name)
                },
                onProfileClick = {
                    navController.navigate(Screens.PROFILE.name)
                },
                onQrCodeClick = {
                    if (!hasCameraPermission) {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    } else navController.navigate(route = Screens.QRSCAN.name)
                },
                onScheduleClick = {
                    // Уже на экране расписания, можно обновить данные или ничего не делать
                    scheduleScreenViewModel.processCommand(
                        ScheduleCommands.GoToToday
                    )
                },
                onCareerClick = {
                    navController.navigate(Screens.CAREER.name)
                },
                onLogoutClick = {
                    authViewModel.processCommand(AuthCommands.Logout)
                    navController.popBackStack(Screens.SCHEDULE.name, inclusive = true)
                    navController.navigate(Screens.WELCOME.name)
                },
                funDialog = {
                    // в зависимости от состояния передаем функцию
                    when (dialogState) {
                        MarkDialogState.DEFAULT -> Spacer(Modifier)
                        MarkDialogState.SUCCESS -> {
                            var flag by remember { mutableStateOf(false) }
                            LaunchedEffect(true) {
                                delay(3000) //имитирует время запроса на сервер
                                flag = true
                            }

                            if (!flag) MarkView()
                            else MarkDialog(onClickConfirm = {
                                dialogState = MarkDialogState.DEFAULT
                            })
                        }

                        MarkDialogState.ERROR -> ErrorMarkDialog(
                            onClickConfirm = { dialogState = MarkDialogState.DEFAULT },
                            onClickDismiss = {
                                dialogState = MarkDialogState.DEFAULT
                                navController.navigate(Screens.QRSCAN.name)
                            })
                    }

                }
            )
        }

        // Экран карьеры
        composable(route = Screens.CAREER.name) {
            CareerScreen(
                viewModel = careerViewModel,
                onQrCodeClick = {
                    if (!hasCameraPermission) {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    } else {
                        navController.navigate(route = Screens.QRSCAN.name)
                    }
                },
                onProfileClick = {
                    navController.navigate(Screens.PROFILE.name)
                },
                onScheduleClick = {
                    navController.popBackStack(Screens.SCHEDULE.name, inclusive = false)
                    navController.navigate(Screens.SCHEDULE.name)
                },
                onVacancyClick = { vacancyId ->
                    navController.navigate("${Screens.VACANCY_DETAILS.name}/$vacancyId")
                }
            )
        }

        // Экран деталей урока
        composable(route = Screens.LESSON.name) {
            LessonDetailsScreen(
                viewModel = lessonViewModel,
                onProfileClick = {
                    navController.navigate(Screens.PROFILE.name)
                },
                onQrCodeClick = {
                    if (!hasCameraPermission) {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    } else navController.navigate(route = Screens.QRSCAN.name)
                },
                onBackPressed = { navController.popBackStack() },
                onScheduleClick = {
                    // Возвращаемся на экран расписания
                    navController.popBackStack(Screens.SCHEDULE.name, inclusive = false)
                },
                onCareerClick = {
                    navController.navigate(Screens.CAREER.name)
                },
                funDialog = {
                    when (dialogState) {
                        MarkDialogState.DEFAULT -> Text(text = "")
                        MarkDialogState.SUCCESS -> {
                            var flag by remember { mutableStateOf(false) }
                            LaunchedEffect(true) {
                                delay(5000)
                                flag = true
                            }

                            if (!flag) MarkView()
                            else MarkDialog(onClickConfirm = {
                                dialogState = MarkDialogState.DEFAULT
                            })
                        }

                        MarkDialogState.ERROR -> ErrorMarkDialog(
                            onClickConfirm = { dialogState = MarkDialogState.DEFAULT },
                            onClickDismiss = {
                                dialogState = MarkDialogState.DEFAULT
                                navController.navigate(Screens.QRSCAN.name)
                            })
                    }
                }
            )
        }

        // Экран деталей вакансии
        composable(
            route = "${Screens.VACANCY_DETAILS.name}/{vacancyId}",
            arguments = listOf(navArgument("vacancyId") { type = NavType.StringType })
        ) { backStackEntry ->
            val vacancyId = backStackEntry.arguments?.getString("vacancyId")

            if (vacancyId != null) {
                val viewModel: VacancyDetailsViewModel = hiltViewModel()

                VacancyDetailsScreen(
                    viewModel = viewModel,
                    vacancyId = vacancyId,
                    onBackPressed = { navController.popBackStack() },
                    onProfileClick = {
                        navController.navigate(Screens.PROFILE.name)
                    },
                    onScheduleClick = {
                        navController.navigate(Screens.SCHEDULE.name) {
                            popUpTo(Screens.SCHEDULE.name) { inclusive = true }
                        }
                    },
                    onCareerClick = {
                        navController.navigate(Screens.CAREER.name) {
                            popUpTo(Screens.CAREER.name) { inclusive = true }
                        }
                    },
                    onQrCodeClick = {
                        if (!hasCameraPermission) {
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        } else {
                            navController.navigate(route = Screens.QRSCAN.name)
                        }
                    },
                )
            }
        }

        // Экран профиля
        composable(route = Screens.PROFILE.name) {
            ProfileScreen(
                onLogoutClick = {
                    authViewModel.processCommand(AuthCommands.Logout)
                    navController.popBackStack(Screens.PROFILE.name, inclusive = true)
                    navController.navigate(Screens.WELCOME.name)
                },
                onScheduleClick = {
                    navController.navigate(Screens.SCHEDULE.name) {
                        popUpTo(Screens.SCHEDULE.name) { inclusive = true }
                    }
                },
                onCareerClick = {
                    navController.navigate(Screens.CAREER.name) {
                        popUpTo(Screens.CAREER.name) { inclusive = true }
                    }
                },
                onQrCodeClick = {
                    if (!hasCameraPermission) {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    } else {
                        navController.navigate(route = Screens.QRSCAN.name)
                    }
                }
            )
        }


        // Экран QR сканера
        composable(route = Screens.QRSCAN.name) {
            LaunchedEffect(true) {
                delay(2500)
                navController.popBackStack()
                val randomInt = (0..1).random()
                when (randomInt) {
                    0 -> dialogState = MarkDialogState.SUCCESS
                    1 -> dialogState = MarkDialogState.ERROR
                }
            }
            QrScanView(
                onClickCancel = { navController.popBackStack() },
                isDetectedQrCode = {}
            )
        }
    }
}