package ru.bogatyreva.class_schedule.presentation.screens.vacancies.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.bogatyreva.class_schedule.R
import ru.bogatyreva.class_schedule.domain.model.UploadedFile
import ru.bogatyreva.class_schedule.presentation.ui.theme.BlueToday
import ru.bogatyreva.class_schedule.presentation.ui.theme.BorderUnfocused
import ru.bogatyreva.class_schedule.presentation.ui.theme.Inter
import ru.bogatyreva.class_schedule.presentation.ui.theme.TitleText

@Composable
fun RespondContent(
    modifier: Modifier = Modifier,
    resumeLink: String,
    onResumeLinkChange: (String) -> Unit,
    isResumeLinkFocused: Boolean,
    onResumeLinkFocusChange: (Boolean) -> Unit,
    coverLetter: String,
    onCoverLetterChange: (String) -> Unit,
    isCoverLetterFocused: Boolean,
    onCoverLetterFocusChange: (Boolean) -> Unit,
    uploadedFile: UploadedFile?,
    fileFormatError: String?,
    onUploadFile: () -> Unit,
    onDeleteFile: () -> Unit,
    onSubmit: () -> Unit,
    isFormValid: Boolean
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Резюме
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row {
                Text(
                    text = stringResource(R.string.bottom_sheet_resume_section),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = Inter,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        letterSpacing = 0.15.sp,
                        color = Color(0xFF1D1B20)
                    )
                )
                Text(
                    text = stringResource(R.string.bottom_sheet_required_star),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = Inter,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        letterSpacing = 0.15.sp,
                        color = Color(0xFFFF5F57)
                    )
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(
                        width = 1.dp,
                        color = if (isResumeLinkFocused) BlueToday else BorderUnfocused,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background(Color.White)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                BasicTextField(
                    value = resumeLink,
                    onValueChange = onResumeLinkChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            onResumeLinkFocusChange(focusState.isFocused)
                        },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        fontFamily = Inter,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        letterSpacing = 0.sp,
                        color = TitleText
                    ),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (resumeLink.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.bottom_sheet_resume_hint),
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

            Text(
                text = stringResource(R.string.bottom_sheet_resume_upload_hint),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = Inter,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    letterSpacing = 0.sp,
                    color = Color(0xFF1D1B20)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            if (uploadedFile != null) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    UploadedFileItem(
                        fileName = uploadedFile.fileName,
                        fileSize = uploadedFile.fileSize,
                        fileType = uploadedFile.fileType,
                        onDelete = onDeleteFile
                    )

                    if (fileFormatError != null) {
                        Text(
                            text = fileFormatError!!,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = Inter,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                letterSpacing = 0.sp,
                                color = Color(0xFFE65443)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp)
                        )
                    }
                }
            } else {
                Button(
                    onClick = onUploadFile,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF7290DD).copy(alpha = 0.10f),
                        contentColor = Color(0xFF4A4459)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_upload),
                            contentDescription = stringResource(R.string.ic_upload_description),
                            modifier = Modifier.size(20.dp),
                            tint = Color(0xFF4A4459)
                        )
                        Text(
                            text = stringResource(R.string.bottom_sheet_upload_button),
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontFamily = Inter,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                letterSpacing = 0.1.sp,
                                color = Color(0xFF4A4459)
                            )
                        )
                    }
                }
            }
        }

        // Сопроводительное письмо
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.bottom_sheet_cover_letter_section),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = Inter,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    letterSpacing = 0.15.sp,
                    color = Color(0xFF1D1B20)
                )
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(132.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(
                        width = 1.dp,
                        color = if (isCoverLetterFocused) BlueToday else BorderUnfocused,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                BasicTextField(
                    value = coverLetter,
                    onValueChange = onCoverLetterChange,
                    modifier = Modifier
                        .fillMaxSize()
                        .onFocusChanged { focusState ->
                            onCoverLetterFocusChange(focusState.isFocused)
                        },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        fontFamily = Inter,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        letterSpacing = 0.sp,
                        color = TitleText
                    ),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.TopStart
                        ) {
                            if (coverLetter.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.bottom_sheet_cover_letter_hint),
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontFamily = Inter,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 16.sp,
                                        lineHeight = 24.sp,
                                        letterSpacing = 0.sp,
                                        color = Color(0xFF767A7F)
                                    )
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
        }

        Button(
            onClick = onSubmit,
            enabled = isFormValid,
            colors = ButtonDefaults.buttonColors(
                containerColor = BlueToday,
                contentColor = Color.White,
                disabledContainerColor = Color(0xFF1D1B20).copy(alpha = 0.10f),
                disabledContentColor = Color(0xFF1D1B20)
            ),
            shape = RoundedCornerShape(100.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = stringResource(R.string.bottom_sheet_button_respond),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = Inter,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    lineHeight = 24.sp,
                    letterSpacing = 0.15.sp,
                    color = if (isFormValid) Color.White else Color(0xFF1D1B20)
                )
            )
        }
    }
}
