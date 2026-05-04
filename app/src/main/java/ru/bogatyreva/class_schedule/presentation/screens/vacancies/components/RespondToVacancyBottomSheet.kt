package ru.bogatyreva.class_schedule.presentation.screens.vacancies.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.bogatyreva.class_schedule.R
import ru.bogatyreva.class_schedule.domain.model.UploadedFile
import ru.bogatyreva.class_schedule.domain.model.UploadedResumeFile
import ru.bogatyreva.class_schedule.presentation.ui.theme.BlueToday
import ru.bogatyreva.class_schedule.presentation.ui.theme.Inter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RespondToVacancyBottomSheet(
    onDismiss: () -> Unit,
    onSubmit: (resumeLink: String, coverLetter: String) -> Unit,
    onGetRandomFile: () -> UploadedResumeFile,
    respondState: RespondState = RespondState.Idle,
    onBackToVacancies: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { true }
    )

    var resumeLink by remember { mutableStateOf("") }
    var coverLetter by remember { mutableStateOf("") }
    var isResumeLinkFocused by remember { mutableStateOf(false) }
    var isCoverLetterFocused by remember { mutableStateOf(false) }

    // Состояние для загруженного файла
    var uploadedFile by remember { mutableStateOf<UploadedFile?>(null) }

    // Состояние ошибки формата файла
    var fileFormatError by remember { mutableStateOf<String?>(null) }

    // Условие активности кнопки
    val isFormValid = (resumeLink.isNotBlank() || (uploadedFile != null && fileFormatError == null)) && respondState == RespondState.Idle

    ModalBottomSheet(
        onDismissRequest = { if (respondState == RespondState.Idle) onDismiss() },
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = Color.White,
        tonalElevation = 0.dp,
        scrimColor = Color.Black.copy(alpha = 0.32f),
        dragHandle = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color(0xFF79747E))
                )
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(546.dp)
                .background(Color.White)
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Заголовок общий для всех состояний
            Text(
                text = stringResource(R.string.bottom_sheet_title),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = Inter,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 22.sp,
                    lineHeight = 28.sp,
                    letterSpacing = 0.sp,
                    color = Color(0xFF0088FF)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // Контент в зависимости от состояния (без заголовка)
            when (respondState) {
                RespondState.Loading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(48.dp),
                            color = BlueToday
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.bottom_sheet_loading_text),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF49454F)
                        )
                    }
                }

                RespondState.Success -> {
                    StatusContent(
                        imageRes = R.drawable.photo_right,
                        imageDescription = stringResource(R.string.bottom_sheet_success_title),
                        mainText = stringResource(R.string.bottom_sheet_success_title),
                        secondaryText = stringResource(R.string.bottom_sheet_success_message),
                        buttonText = stringResource(R.string.bottom_sheet_button_back_to_vacancies),
                        onButtonClick = onBackToVacancies,
                        modifier = Modifier.weight(1f)
                    )
                }

                RespondState.Error -> {
                    StatusContent(
                        imageRes = R.drawable.photo_wrong,
                        imageDescription = stringResource(R.string.bottom_sheet_error_title),
                        mainText = stringResource(R.string.bottom_sheet_error_title),
                        secondaryText = stringResource(R.string.bottom_sheet_error_message),
                        buttonText = stringResource(R.string.bottom_sheet_button_back),
                        onButtonClick = onBack,
                        modifier = Modifier.weight(1f)
                    )
                }

                RespondState.Idle -> {
                    RespondContent(
                        modifier = Modifier.weight(1f),
                        resumeLink = resumeLink,
                        onResumeLinkChange = { resumeLink = it },
                        isResumeLinkFocused = isResumeLinkFocused,
                        onResumeLinkFocusChange = { isResumeLinkFocused = it },
                        coverLetter = coverLetter,
                        onCoverLetterChange = { coverLetter = it },
                        isCoverLetterFocused = isCoverLetterFocused,
                        onCoverLetterFocusChange = { isCoverLetterFocused = it },
                        uploadedFile = uploadedFile,
                        fileFormatError = fileFormatError,
                        onUploadFile = {
                            val randomFile = onGetRandomFile()
                            uploadedFile = UploadedFile(
                                fileName = randomFile.fileName,
                                fileSize = randomFile.fileSize,
                                fileType = randomFile.fileType
                            )
                            fileFormatError = if (randomFile.isValid) null else "Неверный формат файла"
                        },
                        onDeleteFile = {
                            uploadedFile = null
                            fileFormatError = null
                        },
                        onSubmit = {
                            if (isFormValid) {
                                onSubmit(resumeLink, coverLetter)
                            }
                        },
                        isFormValid = isFormValid
                    )
                }
            }
        }
    }
}

// Состояние отправки отклика
sealed class RespondState {
    data object Idle : RespondState()
    data object Loading : RespondState()
    data object Success : RespondState()
    data object Error : RespondState()
}
