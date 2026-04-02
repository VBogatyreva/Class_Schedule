package ru.bogatyreva.class_schedule.domain.model

data class LessonMaterial(
    val id: String,         // Уникальный идентификатор материала
    val fileName: String,   // Имя файла для отображения пользователю "Лекция_История_государства.pdf"
    val fileSize: String,   // Размер файла в формате "3.2 MB"
    val fileUrl: String,    // Ссылка для скачивания файла "https://example.com/file.pdf"
    val fileType: String    // Расширение/тип файла (PDF, DOCX, JPG, LINK)
)
data class SubmittedMaterial(
    val id: String,         // Уникальный идентификатор сданного материала
    val fileName: String,   // Имя файла, который сдал студент "эссе_история.pdf"
    val fileSize: String,   // Размер файла в формате "1.2 MB"
    val fileUrl: String,    // Ссылка на скачивание сданного файла "https://example.com/submission.pdf"
    val fileType: String    // Расширение/тип сданного файла "PDF"
)

