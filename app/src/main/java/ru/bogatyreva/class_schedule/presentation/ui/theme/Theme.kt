package ru.bogatyreva.class_schedule.presentation.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Темная цветовая схема (стандартная)
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

// Светлая цветовая схема (настроенная под ваш дизайн)
private val LightColorScheme = lightColorScheme(
    primary = BluePrimary, // Используем синий из дизайна
    secondary = Orange,    // Используем оранжевый из дизайна
    tertiary = Pink40,

    background = Color.White,
    surface = Color.White,

    onPrimary = Color.White,
    onSecondary = Color.Black,
    onTertiary = Color.White,

    onBackground = TitleText, // Цвет текста на фоне
    onSurface = TitleText,    // Цвет текста на поверхности

    primaryContainer = BluePrimary.copy(alpha = 0.1f), // Полупрозрачный синий
    onPrimaryContainer = BluePrimary,

    secondaryContainer = Orange.copy(alpha = 0.1f),   // Полупрозрачный оранжевый
    onSecondaryContainer = Orange
)

@Composable
fun Class_ScheduleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true, // Dynamic color available on Android 12+
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography, // Используем нашу типографику
        content = content
    )
}