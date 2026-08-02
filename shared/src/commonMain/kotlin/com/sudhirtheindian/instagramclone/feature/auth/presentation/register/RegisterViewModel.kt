package com.sudhirtheindian.instagramclone.feature.auth.presentation.register

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.sudhirtheindian.instagramclone.feature.auth.domain.usecase.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RegisterUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

sealed class RegisterUiEvent {
    data class OnEmailChanged(val email: String) : RegisterUiEvent()
    data class OnPasswordChanged(val password: String) : RegisterUiEvent()
    data class OnUsernameChanged(val username: String) : RegisterUiEvent()
    data class OnFullNameChanged(val fullName: String) : RegisterUiEvent()
    object OnRegisterClicked : RegisterUiEvent()
}

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase
) : ScreenModel {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private var email = ""
    private var password = ""
    private var username = ""
    private var fullName = ""

    fun onEvent(event: RegisterUiEvent) {
        when (event) {
            is RegisterUiEvent.OnEmailChanged -> email = event.email
            is RegisterUiEvent.OnPasswordChanged -> password = event.password
            is RegisterUiEvent.OnUsernameChanged -> username = event.username
            is RegisterUiEvent.OnFullNameChanged -> fullName = event.fullName
            RegisterUiEvent.OnRegisterClicked -> register()
        }
    }

    private fun register() {
        screenModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = registerUseCase(email, password, username, fullName)
            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(isLoading = false, success = true)
            } else {
                _uiState.value = _uiState.value.copy(isLoading = false, error = result.exceptionOrNull()?.message)
            }
        }
    }
}
