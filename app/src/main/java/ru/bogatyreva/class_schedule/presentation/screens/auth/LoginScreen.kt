package ru.bogatyreva.class_schedule.presentation.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.bogatyreva.class_schedule.R
import ru.bogatyreva.class_schedule.presentation.ui.theme.BlueToday
import ru.bogatyreva.class_schedule.presentation.ui.theme.BorderUnfocused
import ru.bogatyreva.class_schedule.presentation.ui.theme.ErrorRed
import ru.bogatyreva.class_schedule.presentation.ui.theme.Inter
import ru.bogatyreva.class_schedule.presentation.ui.theme.ShadowColor
import ru.bogatyreva.class_schedule.presentation.ui.theme.White

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onBackClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.isAuthenticated) {
        if (state.isAuthenticated) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        // Status bar + App bar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(
                        top = 8.dp,
                        start = 4.dp,
                        end = 4.dp,
                        bottom = 8.dp
                    ),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = { onBackClick() })
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Назад",
                        modifier = Modifier.size(24.dp),
                        tint = Color(0xFF222222)
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "Вход в аккаунт",
                    style = TextStyle(
                        fontFamily = Inter,
                        fontWeight = FontWeight.Normal,
                        fontSize = 22.sp,
                        lineHeight = 28.sp,
                        letterSpacing = 0.sp,
                        color = Color(0xFF222222),
                        textAlign = TextAlign.Start
                    )
                )
            }
        }

        // Основной контент
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Текст инструкции или ошибки
            if (state.error != null) {
                Text(
                    text = state.error!!,
                    style = TextStyle(
                        fontFamily = Inter,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        letterSpacing = 0.sp,
                        color = ErrorRed,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                )
            } else {
                Text(
                    text = "Введите номер телефона и пароль",
                    style = TextStyle(
                        fontFamily = Inter,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        letterSpacing = 0.sp,
                        color = Color(0xFF8A8A8E),
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Поле телефона
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(12.dp),
                        spotColor = ShadowColor,
                        ambientColor = ShadowColor
                    ),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                OutlinedTextField(
                    value = state.phoneNumber,
                    onValueChange = { newValue ->
                        viewModel.processCommand(AuthCommands.UpdatePhoneNumber(newValue))
                        if (state.error != null) {
                            viewModel.processCommand(AuthCommands.ClearError)
                        }
                    },
                    placeholder = {
                        Text(
                            text = "+7 (999) 123-45-67",
                            color = if (state.error != null) ErrorRed else Color(0xFF8A8A8E)
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (state.error != null) ErrorRed else BlueToday,
                        unfocusedBorderColor = if (state.error != null) ErrorRed else BorderUnfocused,
                        cursorColor = if (state.error != null) ErrorRed else BlueToday,
                        focusedLabelColor = if (state.error != null) ErrorRed else BlueToday,
                        focusedTextColor = Color(0xFF222222),
                        unfocusedTextColor = Color(0xFF222222),
                        errorBorderColor = ErrorRed,
                        errorSupportingTextColor = ErrorRed
                    ),
                    textStyle = TextStyle(
                        fontFamily = Inter,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        color = Color(0xFF222222)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    singleLine = true,
                    isError = state.error != null,
                    trailingIcon = {
                        if (state.phoneNumber.text.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .pointerInput(Unit) {
                                        detectTapGestures(
                                            onTap = {
                                                viewModel.processCommand(
                                                    AuthCommands.UpdatePhoneNumber(TextFieldValue(""))
                                                )
                                            }
                                        )
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_cancel),
                                    contentDescription = "Очистить",
                                    tint = Color(0xFF8A8A8E),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Поле пароля
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(12.dp),
                        spotColor = ShadowColor,
                        ambientColor = ShadowColor
                    ),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                OutlinedTextField(
                    value = state.password,
                    onValueChange = {
                        viewModel.processCommand(AuthCommands.UpdatePassword(it))
                        if (state.error != null) {
                            viewModel.processCommand(AuthCommands.ClearError)
                        }
                    },
                    placeholder = {
                        Text(
                            text = "Пароль",
                            color = if (state.error != null) ErrorRed else Color(0xFF8A8A8E)
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = androidx.compose.ui.text.input.ImeAction.Done
                    ),
                    visualTransformation = if (state.showPassword) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (state.error != null) ErrorRed else BlueToday,
                        unfocusedBorderColor = if (state.error != null) ErrorRed else BorderUnfocused,
                        cursorColor = if (state.error != null) ErrorRed else BlueToday,
                        focusedLabelColor = if (state.error != null) ErrorRed else BlueToday,
                        focusedTextColor = Color(0xFF222222),
                        unfocusedTextColor = Color(0xFF222222),
                        errorBorderColor = ErrorRed,
                        errorSupportingTextColor = ErrorRed
                    ),
                    textStyle = TextStyle(
                        fontFamily = Inter,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        color = Color(0xFF222222)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    singleLine = true,
                    isError = state.error != null,
                    trailingIcon = {
                        if (state.password.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .pointerInput(Unit) {
                                        detectTapGestures(
                                            onTap = {
                                                viewModel.processCommand(AuthCommands.UpdatePassword(""))
                                            }
                                        )
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_cancel),
                                    contentDescription = "Очистить",
                                    tint = Color(0xFF8A8A8E),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Чекбокс "Показать пароль"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = { viewModel.processCommand(AuthCommands.TogglePasswordVisibility) })
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                // Кастомный чекбокс
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = { viewModel.processCommand(AuthCommands.TogglePasswordVisibility) })
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (state.showPassword) {
                        // Выбранное состояние - синий фон с белой галочкой
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(
                                    color = BlueToday,
                                    shape = RoundedCornerShape(6.dp)
                                )
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_check),
                                contentDescription = "Выбрано",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(16.dp)
                                    .align(Alignment.Center)
                            )
                        }
                    } else {
                        // Не выбранное состояние - прозрачный фон с серой рамкой
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .border(
                                    width = 1.5.dp,
                                    color = Color(0xFFC4C4C7),
                                    shape = RoundedCornerShape(6.dp)
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Текст "Показать пароль"
                Text(
                    text = "Показать пароль",
                    style = TextStyle(
                        fontFamily = Inter,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        letterSpacing = 0.sp,
                        color = Color(0xFF222222)
                    )
                )
            }

            Spacer(modifier = Modifier.height(44.dp))

            // Кнопка "Войти"
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(16.dp),
                        spotColor = if (state.isLoginEnabled) ShadowColor else Color.Transparent,
                        ambientColor = if (state.isLoginEnabled) ShadowColor else Color.Transparent
                    ),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = when {
                        state.isLoading -> Color(0xFF1D1B20).copy(alpha = 0.10f)
                        !state.isLoginEnabled -> Color(0xFF1D1B20).copy(alpha = 0.10f)
                        else -> BlueToday
                    }
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            if (state.isLoginEnabled && !state.isLoading) {
                                detectTapGestures(onTap = { viewModel.processCommand(AuthCommands.Login) })
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        state.isLoading -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color(0xFF1D1B20).copy(alpha = 0.38f)
                            )
                        }
                        else -> {
                            Text(
                                text = "Войти",
                                style = TextStyle(
                                    fontFamily = Inter,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp,
                                    lineHeight = 24.sp,
                                    letterSpacing = 0.15.sp,
                                    color = if (state.isLoginEnabled) Color.White else Color(0xFF1D1B20).copy(alpha = 0.38f)
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Не помню пароль
            Text(
                text = "Не помню пароль",
                style = TextStyle(
                    fontFamily = Inter,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    letterSpacing = 0.sp,
                    color = Color(0xFF1972B2),
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = { onForgotPasswordClick() })
                    }
            )
        }
    }
}