package ru.bogatyreva.class_schedule.presentation.screens.vacancies.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.bogatyreva.class_schedule.presentation.ui.theme.BlueToday
import ru.bogatyreva.class_schedule.presentation.ui.theme.Inter
import ru.bogatyreva.class_schedule.presentation.ui.theme.TitleText

// Общий компонент для экранов успеха и ошибки отправки резюме
@Composable
fun StatusContent(
    imageRes: Int,
    imageDescription: String,
    mainText: String,
    secondaryText: String,
    buttonText: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = imageRes),
            contentDescription = imageDescription,
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = mainText,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = Inter,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    letterSpacing = 0.15.sp,
                    textAlign = TextAlign.Center,
                    color = TitleText
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = secondaryText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = Inter,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    letterSpacing = 0.sp,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF49454F)
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onButtonClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = BlueToday,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(100.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = buttonText,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = Inter,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    lineHeight = 24.sp,
                    letterSpacing = 0.15.sp,
                    color = Color.White
                )
            )
        }
    }
}
