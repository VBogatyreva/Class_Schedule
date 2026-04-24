package ru.bogatyreva.class_schedule.presentation.screens.vacancies.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.flowlayout.FlowRow
import ru.bogatyreva.class_schedule.R
import ru.bogatyreva.class_schedule.presentation.ui.theme.Black
import ru.bogatyreva.class_schedule.presentation.ui.theme.Inter
import ru.bogatyreva.class_schedule.presentation.ui.theme.LessonCardColor

@Preview(showBackground = true)
@Composable
private fun PreviewKeySkillsBlock() {
    KeySkillsBlock(
        list = listOf(
            "Kotlin",
            "Java",
            "Jetpack Compose",
            "UI/UX",
            "Figma",
            "Auto Layout",
            "Prototyping",
            "User Research",
            "Design Systems"
        )
    )
}

@Composable
fun KeySkillsBlock(list: List<String>) {

    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = LessonCardColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Заголовок "Ключевые навыки"
            Text(
                text = stringResource(R.string.key_skills_block),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = Inter,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 22.sp,
                    lineHeight = 28.sp,
                    letterSpacing = 0.sp,
                    color = Black
                )
            )

            // Теги с автоматическим переносом
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                mainAxisSpacing = 8.dp,
                crossAxisSpacing = 8.dp
            ) {
                list.forEach { skill ->
                    CompactTag(text = skill)
                }
            }
        }
    }
}

@Composable
fun CompactTag(text: String) {
    Box(
        modifier = Modifier
            .background(
                color = Color(0xFF7290DD).copy(alpha = 0.1f),
                shape = RoundedCornerShape(5.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontFamily = Inter,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.sp,
                color = Color(0xFF727272)
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}