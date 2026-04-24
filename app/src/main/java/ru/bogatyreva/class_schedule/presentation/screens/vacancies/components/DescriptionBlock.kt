package ru.bogatyreva.class_schedule.presentation.screens.vacancies.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.bogatyreva.class_schedule.R
import ru.bogatyreva.class_schedule.presentation.ui.theme.Black
import ru.bogatyreva.class_schedule.presentation.ui.theme.ConditionTextColor
import ru.bogatyreva.class_schedule.presentation.ui.theme.Inter
import ru.bogatyreva.class_schedule.presentation.ui.theme.VacancyBlockBackGround

@Preview
@Composable
private fun PreviewDescriptionBlock() {
    DescriptionBlock(
        recruitmentRequirements = "Коммерческий опыт UX/UI-дизайна от 2 лет, продуктовая разработка;\n" +
                "Портфолио с реальными кейсами — обязательно;\n" +
                "Уверенное владение Figma (components, variants, Auto Layout);\n" +
                "Понимание UX-процессов, адаптивности и принципов usability;\n" +
                "Базовое понимание frontend-ограничений (HTML/CSS, component-based UI);\n" +
                "Опыт работы в команде, самостоятельность и ответственность.",
        responsibilities = "Проектирование UX/UI для веб-приложений: user flows, wireframes, прототипы, финальные интерфейсы;\n" +
                "Проведение пользовательских исследований и юзабилити-тестов, формирование UX-гипотез;\n" +
                "Создание и поддержка дизайн-системы, работа с компонентами и Auto Layout;\n" +
                "Подготовка макетов и спецификаций для разработки, сопровождение реализации;\n" +
                "Тесная работа с frontend/backend-командой, участие в планировании и ревью;\n" +
                "Улучшение интерфейсов на основе метрик и пользовательской обратной связи.",
        conditions = "Возможно работать удаленно, но кандидаты только из г. Оренбург\n" +
                "Гибкий график, позволяющий совмещать личные дела и карьеру;\n" +
                "Возможности профессионального развития и повышения уровня компетенции;\n" +
                "Конкурентоспособная заработная плата и бонусы за достижение целей."
    )
}

@Composable
fun DescriptionBlock(
    recruitmentRequirements: String,
    responsibilities: String,
    conditions: String,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = VacancyBlockBackGround
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Блок 1: Требования к соискателю
            InfoBlock(
                title = stringResource(R.string.description_recruitment_requirements),
                description = recruitmentRequirements
            )

            // Блок 2: Обязанности
            InfoBlock(
                title = stringResource(R.string.description_responsibilities),
                description = responsibilities
            )

            // Блок 3: Условия
            InfoBlock(
                title = stringResource(R.string.description_conditions),
                description = conditions
            )
        }
    }
}

@Composable
private fun InfoBlock(
    title: String,
    description: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontFamily = Inter,
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                letterSpacing = 0.sp,
                color = Black
            )
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontFamily = Inter,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.sp,
                color = ConditionTextColor
            )
        )

    }
}