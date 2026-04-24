package ru.bogatyreva.class_schedule.presentation.screens.vacancies

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.bogatyreva.class_schedule.R
import ru.bogatyreva.class_schedule.presentation.screens.components.CustomBottomBar
import ru.bogatyreva.class_schedule.presentation.screens.vacancies.components.AddressBlock
import ru.bogatyreva.class_schedule.presentation.screens.vacancies.components.CompanyBlock
import ru.bogatyreva.class_schedule.presentation.screens.vacancies.components.CompanyDescriptionBlock
import ru.bogatyreva.class_schedule.presentation.screens.vacancies.components.ConditionsBlock
import ru.bogatyreva.class_schedule.presentation.screens.vacancies.components.DescriptionBlock
import ru.bogatyreva.class_schedule.presentation.screens.vacancies.components.KeySkillsBlock
import ru.bogatyreva.class_schedule.presentation.screens.vacancies.components.RespondButton
import ru.bogatyreva.class_schedule.presentation.screens.vacancies.components.VacancyTitleBlock
import ru.bogatyreva.class_schedule.presentation.ui.theme.Black
import ru.bogatyreva.class_schedule.presentation.ui.theme.TitleText
import ru.bogatyreva.class_schedule.presentation.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VacancyDetailsScreen(
    viewModel: VacancyDetailsViewModel,
    vacancyId: String,
    onBackPressed: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onScheduleClick: () -> Unit = {},
    onCareerClick: () -> Unit = {},
    onQrCodeClick: () -> Unit = {},
    onClickRespond: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(vacancyId) {
        viewModel.processCommand(VacancyDetailsCommands.LoadVacancyDetails(vacancyId))
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .statusBarsPadding(),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(
                        top = 8.dp,
                        bottom = 8.dp,
                        start = 4.dp,
                        end = 4.dp
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Кнопка назад
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onBackPressed() },
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.material3.Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Назад",
                        modifier = Modifier.size(24.dp),
                        tint = TitleText
                    )
                }

                // Заголовок по центру
                Text(
                    text = stringResource(R.string.vacancy_details_title),
                    style = MaterialTheme.typography.titleLarge,
                    color = Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )

                // Пустой Box для баланса (чтобы заголовок был строго по центру)
                Box(modifier = Modifier.size(48.dp))
            }
        },
        bottomBar = {
            CustomBottomBar(
                onHomeTabClick = onScheduleClick,
                onCareerClick = onCareerClick,
                onProfileClick = onProfileClick,
                onQrCodeClick = onQrCodeClick,
                homeTabActive = false,
                careerTabActive = false,
                profileTabActive = false
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
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color(0xFF3B82F6)
                    )
                }

                state.error != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = state.error!!,
                            color = Color.Red,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Повторить",
                            color = Color(0xFF3B82F6),
                            modifier = Modifier.clickable {
                                viewModel.processCommand(
                                    VacancyDetailsCommands.LoadVacancyDetails(vacancyId)
                                )
                            }
                        )
                    }
                }

                state.vacancy != null -> {
                    val scrollState = rememberScrollState()
                    val vacancy = state.vacancy!!

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                            .verticalScroll(scrollState),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        VacancyTitleBlock(
                            title = vacancy.title,
                            incomeLevel = vacancy.incomeLevel
                        )

                        CompanyBlock(
                            image = vacancy.companyIcon,
                            companyName = vacancy.companyName
                        )

                        ConditionsBlock(
                            workFormat = vacancy.workFormat,
                            employment = vacancy.employment,
                            applyingJob = vacancy.applyingJob,
                            schedule = vacancy.schedule,
                            workingHours = vacancy.workingHours,
                            frequencyOfPayments = vacancy.frequencyOfPayments,
                            experience = vacancy.experience,
                            requiredEducation = vacancy.requiredEducation,
                        )

                        DescriptionBlock(
                            recruitmentRequirements = vacancy.recruitmentRequirements,
                            responsibilities = vacancy.responsibilities,
                            conditions = vacancy.conditions,
                        )

                        KeySkillsBlock(list = vacancy.keySkills)

                        CompanyDescriptionBlock(
                            companyName = vacancy.companyName,
                            companyLocation = vacancy.companyLocation,
                            companyDescription = vacancy.companyDescription
                        )

                        AddressBlock(vacancy.address)

                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }

            // Кнопка "Откликнуться" поверх контента
            if (state.vacancy != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                ) {
                    RespondButton(onClick = onClickRespond)
                }
            }
        }
    }
}