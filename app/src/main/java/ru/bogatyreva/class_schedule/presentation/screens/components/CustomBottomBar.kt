package ru.bogatyreva.class_schedule.presentation.screens.components
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.bogatyreva.class_schedule.R
import ru.bogatyreva.class_schedule.presentation.ui.theme.LightBg

@Composable
fun CustomBottomBar(
    onHomeTabClick: () -> Unit = {},
    onCareerClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onQrCodeClick: () -> Unit = {},
    homeTabActive: Boolean = false,
    careerTabActive: Boolean = false,
    profileTabActive: Boolean = false
) {
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
                modifier = Modifier.weight(1f),
                isActive = homeTabActive,
                icon = R.drawable.ic_calendar,
                label = "Расписание",
                onClick = onHomeTabClick
            )

            // 2. Карьера
            BottomNavItem(
                modifier = Modifier.weight(1f),
                isActive = careerTabActive,
                icon = R.drawable.ic_career,
                label = "Карьера",
                onClick = onCareerClick
            )

            // 3. QR-сканер
            BottomNavItem(
                modifier = Modifier.weight(1f),
                isActive = false,
                icon = R.drawable.ic_scan,
                label = "Скан QR",
                onClick = onQrCodeClick
            )

            // 4. Профиль
            BottomNavItem(
                modifier = Modifier.weight(1f),
                isActive = profileTabActive,
                icon = R.drawable.ic_profile,
                label = "Профиль",
                onClick = onProfileClick
            )
        }
    }
}