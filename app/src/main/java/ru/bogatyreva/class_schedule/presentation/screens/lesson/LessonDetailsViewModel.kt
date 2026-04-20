package ru.bogatyreva.class_schedule.presentation.screens.lesson

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.bogatyreva.class_schedule.domain.model.Assignment
import ru.bogatyreva.class_schedule.domain.model.AttendanceStatus
import ru.bogatyreva.class_schedule.domain.model.Lesson
import ru.bogatyreva.class_schedule.domain.model.LessonMaterial
import ru.bogatyreva.class_schedule.domain.model.SubmittedFile
import ru.bogatyreva.class_schedule.domain.model.SubmittedMaterial
import ru.bogatyreva.class_schedule.domain.repository.ScheduleRepository
import ru.bogatyreva.class_schedule.domain.usecase.GetLessonDetailsUseCase
import ru.bogatyreva.class_schedule.domain.usecase.MarkAttendanceUseCase
import ru.bogatyreva.class_schedule.domain.usecase.SubmitAssignmentUseCase
import javax.inject.Inject

@HiltViewModel
class LessonDetailsViewModel @Inject constructor(
    private val getLessonDetailsUseCase: GetLessonDetailsUseCase,
    private val markAttendanceUseCase: MarkAttendanceUseCase,
    private val submitAssignmentUseCase: SubmitAssignmentUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LessonDetailsScreenState())
    val state = _state.asStateFlow()

    fun processCommand(command: LessonDetailsCommands) {
        viewModelScope.launch {
            when (command) {
                is LessonDetailsCommands.LoadLesson -> {
                    loadLessonDetails(command.lessonId)
                }

                is LessonDetailsCommands.MarkAttendance -> {
                    markAttendance(command.status)
                }

                is LessonDetailsCommands.SubmitAssignment -> {
                    submitAssignment(command.fileUri)
                }

                is LessonDetailsCommands.AddFile -> {
                    addFile(command.file)
                }

                is LessonDetailsCommands.DownloadMaterial -> {
                    downloadMaterial(command.material)
                }

                is LessonDetailsCommands.OpenOnlineLesson -> {
                    openOnlineLesson()
                }

                is LessonDetailsCommands.ClearError -> {
                    clearError()
                }

                is LessonDetailsCommands.ResetState -> {
                    resetState()
                }

                is LessonDetailsCommands.RemoveFile -> {
                    removeFile(command.fileId)
                }
            }
        }
    }

    private suspend fun loadLessonDetails(lessonId: Int) {
        _state.update { it.copy(isLoading = true) }

        try {
            val details = getLessonDetailsUseCase(lessonId)
            _state.update { currentState ->
                currentState.copy(
                    lesson = details.lesson,
                    attendanceStatus = details.attendanceStatus,
                    submittedMaterials = details.submittedMaterials,
                    lessonMaterials = details.lessonMaterials,
                    assignment = details.assignment,
                    isLoading = false,
                    error = null
                )
            }
        } catch (e: Exception) {
            _state.update {
                it.copy(
                    error = "Ошибка загрузки: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    private suspend fun markAttendance(status: AttendanceStatus) {
        val lessonId = _state.value.lesson?.id ?: return
        _state.update { it.copy(isLoading = true) }

        try {
            val result = markAttendanceUseCase(lessonId, status)
            if (result.isSuccess) {
                _state.update { currentState ->
                    currentState.copy(
                        attendanceStatus = status,
                        isLoading = false,
                        error = null
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        error = "Ошибка отметки посещения",
                        isLoading = false
                    )
                }
            }
        } catch (e: Exception) {
            _state.update {
                it.copy(
                    error = "Ошибка: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    private suspend fun submitAssignment(fileUri: String) {
        val lessonId = _state.value.lesson?.id ?: return
        _state.update { it.copy(isLoading = true, isSubmitting = true) }

        try {
            val result = submitAssignmentUseCase(lessonId, fileUri)
            if (result.isSuccess) {
                _state.update { currentState ->
                    val currentAssignment = currentState.assignment
                    currentState.copy(
                        isSubmitting = false,
                        isLoading = false,
                        error = null,
                        // обновляем assignment, меняем submitted на true
                        assignment = currentAssignment?.copy(
                            submitted = true,
                            submittedFiles = currentAssignment.submittedFiles  // файлы остаются
                        ),
                        assignmentSubmitted = true
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        error = "Ошибка отправки задания",
                        isSubmitting = false,
                        isLoading = false
                    )
                }
            }
        } catch (e: Exception) {
            _state.update {
                it.copy(
                    error = "Ошибка: ${e.message}",
                    isSubmitting = false,
                    isLoading = false
                )
            }
        }
    }

    private fun addFile(file: SubmittedFile) {
        val currentAssignment = _state.value.assignment ?: return

        // Добавляем новый файл в список
        val updatedFiles = currentAssignment.submittedFiles + file

        _state.update { currentState ->
            currentState.copy(
                assignment = currentAssignment.copy(
                    submittedFiles = updatedFiles,
                    submitted = false  // после добавления файла задание больше не сдано
                ),
                assignmentSubmitted = false
            )
        }
    }

    private fun downloadMaterial(material: LessonMaterial) {
        _state.update { it.copy(message = "Скачивание: ${material.fileName}") }
    }

    private fun openOnlineLesson() {
        val lesson = _state.value.lesson
        val link = lesson?.onlineLink
        if (lesson?.isOnline == true && !link.isNullOrBlank()) {
            // Открыть ссылку в браузере
            // Пока просто логируем или сохраняем в state
            _state.update { it.copy(message = "Открыть: $link") }
            // TODO: Реализовать открытие ссылки
        }
    }

    private fun removeFile(fileId: String) {
        val currentAssignment = _state.value.assignment ?: return

        // Удаляем файл из списка
        val updatedFiles = currentAssignment.submittedFiles.filter { it.id != fileId }

        // ВСЕГДА устанавливаем submitted = false после удаления
        // (задание нужно обновить, даже если были другие файлы)
        _state.update { currentState ->
            currentState.copy(
                assignment = currentAssignment.copy(
                    submittedFiles = updatedFiles,
                    submitted = false  // всегда false после удаления
                )
            )
        }
    }

    private fun clearError() {
        _state.update { it.copy(error = null, message = null) }
    }

    private fun resetState() {
        _state.update { LessonDetailsScreenState() }
    }
}

sealed interface LessonDetailsCommands {
    data class LoadLesson(val lessonId: Int) : LessonDetailsCommands
    data class MarkAttendance(val status: AttendanceStatus) : LessonDetailsCommands
    data class SubmitAssignment(val fileUri: String) : LessonDetailsCommands
    data class AddFile(val file: SubmittedFile) : LessonDetailsCommands
    data class DownloadMaterial(val material: LessonMaterial) : LessonDetailsCommands
    data object OpenOnlineLesson : LessonDetailsCommands
    data object ClearError : LessonDetailsCommands
    data object ResetState : LessonDetailsCommands
    data class RemoveFile(val fileId: String) : LessonDetailsCommands
}

data class LessonDetailsScreenState(
    val lesson: Lesson? = null,
    val attendanceStatus: AttendanceStatus = AttendanceStatus.NOT_MARKED,
    val submittedMaterials: List<SubmittedMaterial> = emptyList(),
    val lessonMaterials: List<LessonMaterial> = emptyList(),
    val assignment: Assignment? = null,
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val assignmentSubmitted: Boolean = false,
    val error: String? = null,
    val message: String? = null,
    val selectedTab: LessonDetailsTab = LessonDetailsTab.INFO
) {
    val isEmptyState: Boolean
        get() = lesson == null && !isLoading && error == null
}

enum class LessonDetailsTab {
    INFO, MATERIALS, ASSIGNMENT
}