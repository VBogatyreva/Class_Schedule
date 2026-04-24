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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.bogatyreva.class_schedule.presentation.ui.theme.Black
import ru.bogatyreva.class_schedule.presentation.ui.theme.BlueSelected
import ru.bogatyreva.class_schedule.presentation.ui.theme.ConditionTextColor
import ru.bogatyreva.class_schedule.presentation.ui.theme.Inter
import ru.bogatyreva.class_schedule.presentation.ui.theme.VacancyBlockBackGround

@Preview(showBackground = true)
@Composable
private fun PreviewCompanyDescriptionBlock() {
    CompanyDescriptionBlock(
        companyName = "Федеральная мониторинговая компания",
        companyLocation = "Оренбург, Россия",
        companyDescription = "Федеральная мониторинговая компания задает высокие стандарты ведения бизнеса в сфере аналитики и консалтинга, собирает и обрабатывает рыночную информацию, помогает государству развивать промышленные рынки и экономику нашей страны.\n\n" +
                "Наша команда ежедневно собирает рыночные данные по более чем 50-ти направлениям промышленного производства и сельского хозяйства. Систематизируя полученную информацию, мы разрабатываем прогнозы по изменению ситуации в отраслях. Свои отчеты мы визуализируем и формируем в информационные пакеты для удобства работы с ними. Такой мониторинг помогает руководителям и специалистам государственных структур понимать текущую обстановку на рынке и разрабатывать меры по их поддержке и развитию на основе актуальных данных."
    )
}


@Composable
fun CompanyDescriptionBlock(
    companyName: String,
    companyLocation: String,
    companyDescription: String
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
            // Название компании
            Text(
                text = companyName,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = Inter,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 22.sp,
                    lineHeight = 28.sp,
                    letterSpacing = 0.sp,
                    color = BlueSelected
                )
            )

            // Локация компании
            Text(
                text = companyLocation,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = Inter,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    letterSpacing = 0.sp,
                    color = ConditionTextColor
                )
            )

            // Описание компании
            Text(
                text = companyDescription,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = Inter,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    letterSpacing = 0.sp,
                    color = Black
                )
            )
        }
    }
}