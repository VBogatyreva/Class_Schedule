package ru.bogatyreva.class_schedule.domain.model

data class Assignment(
    val id: String,                                          // Уникальный идентификатор задания
    val title: String,                                       // Название/тема задания "Эссе по истории государства"
    val description: String,                                 // Описание задания (что нужно сделать)
    val deadline: String? = null,                            // Срок сдачи задания (может быть null, если не указан)
    val submitted: Boolean = false,                          // Статус сдачи задания (сдано или нет)
    val submittedFiles: List<SubmittedFile> = emptyList()    // Список файлов, которые студент прикрепил при сдаче
)

data class SubmittedFile(
    val id: String,                                          // Уникальный идентификатор файла
    val fileName: String,                                    // Имя файла для отображения пользователю "эссе_история.pdf"
    val fileSize: String,                                    // Размер файла в формате "1.2 MB"
    val fileType: String                                     // Расширение/тип файла "PDF"
)

