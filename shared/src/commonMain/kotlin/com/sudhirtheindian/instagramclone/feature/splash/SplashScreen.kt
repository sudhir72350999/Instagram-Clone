package com.sudhirtheindian.instagramclone.feature.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.sudhirtheindian.instagramclone.core.ui.navigation.InstagramScreen
import com.sudhirtheindian.instagramclone.feature.auth.presentation.login.LoginScreen
import com.sudhirtheindian.instagramclone.feature.home.presentation.home.HomeScreen
import com.sudhirtheindian.instagramclone.feature.splash.components.SplashLogo
import com.sudhirtheindian.instagramclone.feature.splash.presentation.SplashUiState
import com.sudhirtheindian.instagramclone.feature.splash.presentation.SplashViewModel

class SplashScreen : InstagramScreen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<SplashViewModel>()
        val uiState by viewModel.uiState.collectAsState()

        LaunchedEffect(uiState) {
            when (uiState) {
                is SplashUiState.Authenticated -> {
                    navigator.replaceAll(HomeScreen())
                }
                is SplashUiState.Unauthenticated -> {
                    navigator.replaceAll(LoginScreen())
                }
                else -> {}
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            SplashLogo()
        }
    }
}
