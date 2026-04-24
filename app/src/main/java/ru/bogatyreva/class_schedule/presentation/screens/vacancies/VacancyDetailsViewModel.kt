package ru.bogatyreva.class_schedule.presentation.screens.vacancies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.bogatyreva.class_schedule.domain.model.VacancyDetails
import ru.bogatyreva.class_schedule.domain.repository.CareerRepository
import javax.inject.Inject

@HiltViewModel
class VacancyDetailsViewModel @Inject constructor(
    private val repository: CareerRepository
) : ViewModel() {

    private val _state = MutableStateFlow(VacancyDetailsState())
    val state = _state.asStateFlow()

    fun processCommand(command: VacancyDetailsCommands) {
        when (command) {
            is VacancyDetailsCommands.LoadVacancyDetails -> loadVacancyDetails(command.vacancyId)
            is VacancyDetailsCommands.ClearError -> clearError()
        }
    }

    private fun loadVacancyDetails(vacancyId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val vacancy = repository.getVacancyDetailsById(vacancyId)
                if (vacancy != null) {
                    _state.update {
                        it.copy(
                            vacancy = vacancy,
                            isLoading = false,
                            error = null
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = "Вакансия не найдена"
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Ошибка загрузки вакансии"
                    )
                }
            }
        }
    }

    private fun clearError() {
        _state.update { it.copy(error = null) }
    }
}

sealed interface VacancyDetailsCommands {
    data class LoadVacancyDetails(val vacancyId: String) : VacancyDetailsCommands
    data object ClearError : VacancyDetailsCommands
}

data class VacancyDetailsState(
    val vacancy: VacancyDetails? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)