package com.sudhirtheindian.instagramclone.feature.settings.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SettingsUiState(
    val isDarkMode: Boolean = false,
    val language: String = "English",
    val isLoggingOut: Boolean = false
)

class SettingsViewModel : ScreenModel {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun toggleDarkMode(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(isDarkMode = enabled)
    }

    fun setLanguage(language: String) {
        _uiState.value = _uiState.value.copy(language = language)
    }

    fun logout() {
        _uiState.value = _uiState.value.copy(isLoggingOut = true)
        // Actual logout logic would go here
    }
}
