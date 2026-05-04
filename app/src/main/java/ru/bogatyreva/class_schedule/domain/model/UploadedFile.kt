package ru.bogatyreva.class_schedule.domain.model

// Модель данных для загруженного файла
data class UploadedFile(
    val fileName: String,
    val fileSize: String,
    val fileType: String
)

