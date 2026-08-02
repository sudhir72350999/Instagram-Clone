package com.sudhirtheindian.instagramclone.feature.splash.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.sudhirtheindian.instagramclone.feature.auth.domain.usecase.CheckAuthStateUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class SplashUiState {
    object Idle : SplashUiState()
    object Authenticated : SplashUiState()
    object Unauthenticated : SplashUiState()
}

class SplashViewModel(
    private val checkAuthStateUseCase: CheckAuthStateUseCase
) : ScreenModel {

    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Idle)
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    init {
        checkAuthentication()
    }

    private fun checkAuthentication() {
        screenModelScope.launch {
            // Artificial delay for splash branding visibility
            delay(2000)
            val isLoggedIn = checkAuthStateUseCase()
            if (isLoggedIn) {
                _uiState.value = SplashUiState.Authenticated
            } else {
                _uiState.value = SplashUiState.Unauthenticated
            }
        }
    }
}
