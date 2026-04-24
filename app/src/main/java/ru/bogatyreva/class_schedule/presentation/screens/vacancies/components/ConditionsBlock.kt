package ru.bogatyreva.class_schedule.presentation.screens.vacancies.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
private fun PreviewConditionsBlock() {
    ConditionsBlock(
        workFormat = "удаленно",
        employment = "частичная",
        applyingJob = "самозанятость",
        schedule = "5/2",
        workingHours = 8,
        frequencyOfPayments = "2 раза в месяц",
        experience = "от 1 до 3 лет",
        requiredEducation = "высшее",
    )
}

@Composable
fun ConditionsBlock(
    workFormat: String,
    employment: String,
    applyingJob: String,
    schedule: String,
    workingHours: Int,
    frequencyOfPayments: String,
    experience: String,
    requiredEducation: String,
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
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                ConditionTextRow(
                    label = stringResource(R.string.condition_format),
                    value = workFormat
                )

                ConditionTextRow(
                    label = stringResource(R.string.condition_employment),
                    value = employment
                )

                ConditionTextRow(
                    label = stringResource(R.string.condition_applying_job),
                    value = applyingJob
                )

                ConditionTextRow(
                    label = stringResource(R.string.condition_schedule),
                    value = schedule
                )

                ConditionTextRow(
                    label = stringResource(R.string.condition_working_hours),
                    value = workingHours.toString()
                )

                ConditionTextRow(
                    label = stringResource(R.string.condition_frequency_of_payments),
                    value = frequencyOfPayments
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ConditionTextRow(
                    label = stringResource(R.string.condition_experience),
                    value = experience
                )

                ConditionTextRow(
                    label = stringResource(R.string.condition_required_education),
                    value = requiredEducation
                )
            }
        }
    }
}

@Composable
private fun ConditionTextRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontFamily = Inter,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.sp,
                color = ConditionTextColor
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontFamily = Inter,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.sp,
                color = Black
            )
        )

    }
}