package com.sudhirtheindian.instagramclone.feature.auth.presentation.register

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.sudhirtheindian.instagramclone.core.ui.navigation.InstagramScreen
import com.sudhirtheindian.instagramclone.feature.auth.presentation.components.AuthButton
import com.sudhirtheindian.instagramclone.feature.auth.presentation.components.AuthTextField
import com.sudhirtheindian.instagramclone.feature.splash.components.SplashLogo

class RegisterScreen : InstagramScreen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<RegisterViewModel>()
        val uiState by viewModel.uiState.collectAsState()

        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var username by remember { mutableStateOf("") }
        var fullName by remember { mutableStateOf("") }

        LaunchedEffect(uiState.success) {
            if (uiState.success) {
                // navigator.replaceAll(HomeScreen())
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SplashLogo(modifier = Modifier.padding(bottom = 32.dp))

                AuthTextField(
                    value = email,
                    onValueChange = { 
                        email = it
                        viewModel.onEvent(RegisterUiEvent.OnEmailChanged(it))
                    },
                    label = "Email"
                )

                Spacer(modifier = Modifier.height(16.dp))

                AuthTextField(
                    value = fullName,
                    onValueChange = { 
                        fullName = it
                        viewModel.onEvent(RegisterUiEvent.OnFullNameChanged(it))
                    },
                    label = "Full Name"
                )

                Spacer(modifier = Modifier.height(16.dp))

                AuthTextField(
                    value = username,
                    onValueChange = { 
                        username = it
                        viewModel.onEvent(RegisterUiEvent.OnUsernameChanged(it))
                    },
                    label = "Username"
                )

                Spacer(modifier = Modifier.height(16.dp))

                AuthTextField(
                    value = password,
                    onValueChange = { 
                        password = it
                        viewModel.onEvent(RegisterUiEvent.OnPasswordChanged(it))
                    },
                    label = "Password",
                    isPassword = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                AuthButton(
                    text = "Sign Up",
                    isLoading = uiState.isLoading,
                    onClick = { viewModel.onEvent(RegisterUiEvent.OnRegisterClicked) }
                )

                uiState.error?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        }
    }
}
