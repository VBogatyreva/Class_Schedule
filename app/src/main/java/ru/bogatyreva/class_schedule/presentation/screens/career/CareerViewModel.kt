package ru.bogatyreva.class_schedule.presentation.screens.career

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.bogatyreva.class_schedule.domain.model.Vacancy
import ru.bogatyreva.class_schedule.domain.usecase.GetVacanciesUseCase
import javax.inject.Inject

@HiltViewModel
class CareerViewModel @Inject constructor(
    private val getVacanciesUseCase: GetVacanciesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CareerScreenState())
    val state = _state.asStateFlow()

    init {
        loadVacancies()
    }

    fun processCommand(command: CareerCommands) {
        when (command) {
            is CareerCommands.LoadVacancies -> loadVacancies()
            is CareerCommands.OnVacancyClick -> onVacancyClick(command.vacancyId)
            is CareerCommands.ClearError -> clearError()
        }
    }

    private fun loadVacancies() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            getVacanciesUseCase()
                .catch { exception ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Ошибка загрузки вакансий"
                        )
                    }
                }
                .collect { vacancies ->
                    _state.update {
                        it.copy(
                            vacancies = vacancies,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    private fun onVacancyClick(vacancyId: String) {
        _state.update { it.copy(selectedVacancyId = vacancyId) }
    }

    private fun clearError() {
        _state.update { it.copy(error = null) }
    }
}

sealed interface CareerCommands {
    data object LoadVacancies : CareerCommands
    data class OnVacancyClick(val vacancyId: String) : CareerCommands
    data object ClearError : CareerCommands
}

data class CareerScreenState(
    val vacancies: List<Vacancy> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedVacancyId: String? = null
)