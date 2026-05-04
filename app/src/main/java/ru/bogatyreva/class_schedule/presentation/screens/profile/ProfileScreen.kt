package ru.bogatyreva.class_schedule.presentation.screens.profile

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.bogatyreva.class_schedule.R
import ru.bogatyreva.class_schedule.presentation.screens.components.CustomBottomBar
import ru.bogatyreva.class_schedule.presentation.ui.theme.Inter
import ru.bogatyreva.class_schedule.presentation.ui.theme.White
import ru.bogatyreva.class_schedule.utils.ResultState


@Preview(showBackground = true)
@Composable
private fun PreviewProfileScreen() {
    ProfileScreen()
}

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onLogoutClick: () -> Unit = {},
    onScheduleClick: () -> Unit = {},
    onCareerClick: () -> Unit = {},
    onQrCodeClick: () -> Unit = {}
) {
    val studentState by viewModel.student.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .statusBarsPadding(),
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 18.dp,
                        start = 16.dp,
                        end = 16.dp,
                    )
            ) {
                Text(
                    text = stringResource(R.string.profile_title),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = Inter,
                        fontWeight = FontWeight.Normal,
                        fontSize = 22.sp,
                        lineHeight = 28.sp,
                        letterSpacing = 0.sp,
                        textAlign = TextAlign.Center,
                        color = Color(0xFF1D1B20)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        bottomBar = {
            CustomBottomBar(
                onHomeTabClick = onScheduleClick,
                onCareerClick = onCareerClick,
                onProfileClick = {},
                onQrCodeClick = onQrCodeClick,
                homeTabActive = false,
                careerTabActive = false,
                profileTabActive = true
            )
        },
        containerColor = White
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    // Центрированный индикатор загрузки (как на других экранах)
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFF0088FF))
                    }
                }

                studentState is ResultState.Error -> {
                    // Сообщение об ошибке по центру
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = (studentState as ResultState.Error).msg,
                                color = Color.Red,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            TextButton(onClick = { viewModel.loadCurrentStudent() }) {
                                Text("Повторить")
                            }
                        }
                    }
                }

                studentState is ResultState.Success -> {
                    val student = (studentState as ResultState.Success).data

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))

                        // Информация Профиля (аватар + имя + телефон)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFF0F0F0)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.photo_profile),
                                    contentDescription = stringResource(R.string.avatar_profile),
                                    modifier = Modifier.size(28.dp),
                                    tint = Color(0xFF222222)
                                )
                            }

                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                // ФИО
                                Text(
                                    text = student.fullName,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontFamily = Inter,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 16.sp,
                                        lineHeight = 24.sp,
                                        letterSpacing = 0.sp,
                                        color = Color(0xFF1D1B20)
                                    )
                                )
                                // Телефон
                                Text(
                                    text = student.formattedPhone,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontFamily = Inter,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 14.sp,
                                        lineHeight = 20.sp,
                                        letterSpacing = 0.1.sp,
                                        color = Color(0xFF767A7F)
                                    )
                                )
                            }
                        }

                        // Divider
                        Divider(
                            modifier = Modifier.padding(horizontal = 0.dp),
                            color = Color(0xFFEFEFF2),
                            thickness = 1.dp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Выйти из аккаунта
                        TextButton(
                            onClick = { showLogoutDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(100.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_logout),
                                    contentDescription = stringResource(R.string.logout_icon_description),
                                    modifier = Modifier.size(20.dp),
                                    tint = Color.Unspecified
                                )
                                Text(
                                    text = stringResource(R.string.logout_button_text),
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontFamily = Inter,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 14.sp,
                                        lineHeight = 20.sp,
                                        letterSpacing = 0.1.sp,
                                        color = Color(0xFFE65443)
                                    )
                                )
                            }
                        }
                    }
                }

                else -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFF0088FF))
                    }
                }
            }
        }
    }

    // Диалог подтверждения выхода
    if (showLogoutDialog) {
        LogoutDialog(
            onDismiss = { showLogoutDialog = false },
            onConfirm = {
                showLogoutDialog = false
                onLogoutClick()
            }
        )
    }
}
