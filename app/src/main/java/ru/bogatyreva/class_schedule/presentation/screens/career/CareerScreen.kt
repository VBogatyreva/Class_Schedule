package ru.bogatyreva.class_schedule.presentation.screens.career

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.flowlayout.FlowRow
import ru.bogatyreva.class_schedule.R
import ru.bogatyreva.class_schedule.domain.model.Vacancy
import ru.bogatyreva.class_schedule.presentation.screens.components.CustomBottomBar
import ru.bogatyreva.class_schedule.presentation.ui.theme.BlueToday
import ru.bogatyreva.class_schedule.presentation.ui.theme.Inter
import ru.bogatyreva.class_schedule.presentation.ui.theme.LessonCardColor
import ru.bogatyreva.class_schedule.presentation.ui.theme.TitleText
import ru.bogatyreva.class_schedule.presentation.ui.theme.White


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CareerScreen(
    viewModel: CareerViewModel,
    onQrCodeClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onScheduleClick: () -> Unit = {},
    onVacancyClick: (String) -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }

    // Фильтрация вакансий по поисковому запросу
    val filteredVacancies = if (searchQuery.isBlank()) {
        state.vacancies
    } else {
        val query = searchQuery.trim().lowercase()
        state.vacancies.filter { vacancy ->
            vacancy.title.lowercase().contains(query)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .statusBarsPadding(),

        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(top = 16.dp, bottom = 8.dp)
            ) {
                Text(
                    text = "Карьера",
                    style = MaterialTheme.typography.displaySmall,
                    color = TitleText,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                )
            }
        },
        bottomBar = {
            CustomBottomBar(
                onHomeTabClick = onScheduleClick,
                onCareerClick = { },
                onProfileClick = onProfileClick,
                onQrCodeClick = onQrCodeClick,
                homeTabActive = false,
                careerTabActive = true,
                profileTabActive = false
            )
        },
        containerColor = White
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
                .padding(paddingValues)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = BlueToday
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
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Повторить",
                            color = BlueToday,
                            modifier = Modifier.clickable {
                                viewModel.processCommand(CareerCommands.LoadVacancies)
                            }
                        )
                    }
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(White)
                    ) {
                        Spacer(modifier = Modifier.height(20.dp))

                        SearchField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        if (filteredVacancies.isEmpty() && searchQuery.isNotBlank()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Ничего не найдено",
                                    color = TitleText.copy(alpha = 0.6f),
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Попробуйте изменить запрос",
                                    color = TitleText.copy(alpha = 0.4f),
                                    fontSize = 14.sp
                                )
                            }
                        } else if (filteredVacancies.isEmpty()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Нет доступных вакансий",
                                    color = TitleText.copy(alpha = 0.6f)
                                )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                                    horizontal = 16.dp,
                                    vertical = 0.dp
                                ),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(filteredVacancies) { vacancy ->
                                    VacancyCard(
                                        vacancy = vacancy,
                                        onClick = {
                                            viewModel.processCommand(
                                                CareerCommands.OnVacancyClick(vacancy.id)
                                            )
                                            onVacancyClick(vacancy.id)
                                        }
                                    )
                                }

                                item {
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .background(
                color = Color(0xFF747480).copy(alpha = 0.08f),
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            Box(
                modifier = Modifier.size(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "Поиск",
                    modifier = Modifier.size(18.dp),
                    tint = Color(0xFF1C1B1F)
                )
            }

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = Inter,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    letterSpacing = 0.sp,
                    color = TitleText
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = "Найти вакансию",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontFamily = Inter,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 16.sp,
                                    lineHeight = 24.sp,
                                    letterSpacing = 0.sp,
                                    color = Color(0xFF8A8A8E)
                                )
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
    }
}

@Composable
fun VacancyCard(
    vacancy: Vacancy,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LessonCardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Изображение вакансии - показываем ТОЛЬКО если есть URL
            if (!vacancy.imageUrl.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFE0E0E0))
                ) {
                    Image(
                        painter = painterResource(R.drawable.photo_job),
                        contentDescription = "Изображение вакансии ${vacancy.title}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Строка с названием вакансии и иконкой перехода
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = vacancy.title,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontFamily = Inter,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 22.sp,
                        lineHeight = 28.sp,
                        letterSpacing = 0.sp
                    ),
                    color = TitleText,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_forward),
                    contentDescription = "Подробнее",
                    modifier = Modifier.size(8.dp, 14.dp),
                    tint = Color.Unspecified
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Название компании
            Text(
                text = vacancy.companyName,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = Inter,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    letterSpacing = 0.15.sp,
                    color = Color(0xFF727272)
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Строка с зарплатой и "на руки" в одной строке
            if (vacancy.salaryMin != null || vacancy.salaryMax != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = vacancy.salaryDisplay,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = Inter,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            letterSpacing = 0.15.sp,
                            color = TitleText
                        )
                    )

                    Text(
                        text = "на руки",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontFamily = Inter,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            letterSpacing = 0.sp,
                            color = Color(0xFF727272)
                        )
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            } else {
                Text(
                    text = vacancy.salaryDisplay,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = Inter,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        letterSpacing = 0.15.sp,
                        color = TitleText
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // ТЕГИ С АВТОМАТИЧЕСКИМ ПЕРЕНОСОМ
            // Собираем все теги в один список
            val allTags = buildList {
                add(vacancy.employmentType.lowercase())
                add(vacancy.experienceDisplay)
                if (vacancy.isRemote) add("удаленно")
                if (vacancy.isOnSite) add("на месте работодателя")
                addAll(vacancy.tags.map { it.lowercase() })
            }

            // Отображаем теги с переносом на новую строку
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                mainAxisSpacing = 8.dp,
                crossAxisSpacing = 8.dp
            ) {
                allTags.forEach { tag ->
                    CompactTag(text = tag)
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