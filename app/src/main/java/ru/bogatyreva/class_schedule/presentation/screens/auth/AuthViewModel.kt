package ru.bogatyreva.class_schedule.presentation.screens.auth

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.bogatyreva.class_schedule.domain.model.AuthResult
import ru.bogatyreva.class_schedule.domain.model.AuthState
import ru.bogatyreva.class_schedule.domain.repository.AuthRepository
import ru.bogatyreva.class_schedule.domain.usecase.LoginUseCase
import ru.bogatyreva.class_schedule.domain.usecase.LogoutUseCase
import ru.bogatyreva.class_schedule.utils.PhoneNumberFormatter
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow()

    init {
        checkAutoLogin()
    }

    private fun checkAutoLogin() {
        viewModelScope.launch {
            val isAuthenticated = authRepository.isAuthenticated()
            if (isAuthenticated) {
                _state.update {
                    it.copy(
                        isAuthenticated = true,
                        isLoading = false
                    )
                }
            }
        }
    }


    fun processCommand(command: AuthCommands) {
        when (command) {
            is AuthCommands.UpdatePhoneNumber -> updatePhoneNumber(command.textFieldValue)
            is AuthCommands.UpdatePassword -> updatePassword(command.password)
            is AuthCommands.TogglePasswordVisibility -> togglePasswordVisibility()
            is AuthCommands.Login -> login()
            is AuthCommands.Logout -> logout()
            is AuthCommands.ClearError -> clearError()
        }
    }

    private fun updatePhoneNumber(textFieldValue: TextFieldValue) {
        val formatted = PhoneNumberFormatter.format(textFieldValue)
        _state.update {
            it.copy(
                phoneNumber = formatted,
                error = null
            )
        }
    }

    private fun updatePassword(password: String) {
        _state.update { it.copy(password = password, error = null) }
    }

    private fun togglePasswordVisibility() {
        _state.update { it.copy(showPassword = !it.showPassword) }
    }

    private fun login() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val rawPhoneNumber = PhoneNumberFormatter.extractDigits(_state.value.phoneNumber.text)
            val result = loginUseCase(rawPhoneNumber, _state.value.password)

            when (result) {
                is AuthResult.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isAuthenticated = true,
                            error = null
                        )
                    }
                }
                is AuthResult.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isAuthenticated = false,
                            error = result.message
                        )
                    }
                }
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            _state.update { AuthState() }
        }
    }

    private fun clearError() {
        _state.update { it.copy(error = null) }
    }
}

sealed interface AuthCommands {
    data class UpdatePhoneNumber(val textFieldValue: TextFieldValue) : AuthCommands
    data class UpdatePassword(val password: String) : AuthCommands
    data object TogglePasswordVisibility : AuthCommands
    data object Login : AuthCommands
    data object Logout : AuthCommands
    data object ClearError : AuthCommands
}