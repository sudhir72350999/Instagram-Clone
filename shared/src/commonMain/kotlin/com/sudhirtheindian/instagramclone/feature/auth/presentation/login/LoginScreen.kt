package com.sudhirtheindian.instagramclone.feature.auth.presentation.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.sudhirtheindian.instagramclone.core.ui.navigation.InstagramScreen
import com.sudhirtheindian.instagramclone.feature.auth.presentation.components.AuthButton
import com.sudhirtheindian.instagramclone.feature.auth.presentation.components.AuthTextField
import com.sudhirtheindian.instagramclone.feature.auth.presentation.register.RegisterScreen
import com.sudhirtheindian.instagramclone.feature.splash.components.SplashLogo

class LoginScreen : InstagramScreen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<LoginViewModel>()
        val uiState by viewModel.uiState.collectAsState()

        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        LaunchedEffect(uiState.success) {
            if (uiState.success) {
                // navigator.replaceAll(HomeScreen())
            }
        }

        Scaffold(
            bottomBar = {
                // Instagram-style bottom footer for sign up
                Column {
                    HorizontalDivider(
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Don't have an account? ",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        TextButton(
                            onClick = { navigator.push(RegisterScreen()) },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = "Sign up.",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Instagram Logo
                    SplashLogo(
                        modifier = Modifier
                            .size(72.dp)
                            .padding(bottom = 40.dp)
                    )

                    // Email / Username Field
                    AuthTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            viewModel.onEvent(LoginUiEvent.OnEmailChanged(it))
                        },
                        label = "Phone number, username, or email"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Password Field
                    AuthTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            viewModel.onEvent(LoginUiEvent.OnPasswordChanged(it))
                        },
                        label = "Password",
                        isPassword = true
                    )

                    // Forgot Password link (Aligned to right, typical for IG)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp, bottom = 24.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        TextButton(
                            onClick = { /* TODO: Handle Forgot Password */ },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = "Forgot password?",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    // Log In Button
                    AuthButton(
                        text = "Log in",
                        isLoading = uiState.isLoading,
                        onClick = { viewModel.onEvent(LoginUiEvent.OnLoginClicked) }
                    )

                    // Error Message
                    uiState.error?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    // Wrap with your app theme and Voyager's Navigator container
    // so `LocalNavigator` and `getScreenModel` find their providers.
    MaterialTheme {
        Navigator(LoginScreen())
    }
}