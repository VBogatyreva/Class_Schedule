package ru.bogatyreva.class_schedule.domain.model

//Модель загруженного файла резюме
data class UploadedResumeFile(
    val fileName: String,
    val fileSize: String,
    val fileType: String,
    val isValid: Boolean
)
