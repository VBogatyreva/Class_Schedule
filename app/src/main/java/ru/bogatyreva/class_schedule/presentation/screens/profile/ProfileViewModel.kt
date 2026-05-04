package ru.bogatyreva.class_schedule.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.bogatyreva.class_schedule.domain.model.Student
import ru.bogatyreva.class_schedule.domain.repository.ProfileRepository
import ru.bogatyreva.class_schedule.utils.ResultState
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _student = MutableStateFlow<ResultState<Student>>(ResultState.Default)
    val student get() = _student.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading get() = _isLoading.asStateFlow()

    init {
        loadCurrentStudent()
    }

    fun loadCurrentStudent() {
        viewModelScope.launch {
            _isLoading.value = true
            _student.value = ResultState.Default
            try {
                val student = repository.getCurrentStudent()
                if (student != null) {
                    _student.value = ResultState.Success(student)
                } else {
                    _student.value = ResultState.Error("Не удалось загрузить данные профиля")
                }
            } catch (e: Exception) {
                _student.value = ResultState.Error(e.message ?: "Ошибка загрузки")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
