package com.sudhirtheindian.instagramclone.feature.auth

import com.sudhirtheindian.instagramclone.feature.auth.domain.model.User
import com.sudhirtheindian.instagramclone.feature.auth.domain.repository.AuthRepository
import com.sudhirtheindian.instagramclone.feature.auth.domain.usecase.LoginUseCase
import com.sudhirtheindian.instagramclone.feature.auth.presentation.login.LoginUiEvent
import com.sudhirtheindian.instagramclone.feature.auth.presentation.login.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `login should update state to success on valid credentials`() = runTest {
        val repository = object : AuthRepository {
            override fun getCurrentUser(): Flow<User?> = flowOf(null)
            override suspend fun isUserLoggedIn(): Boolean = false
            override suspend fun login(email: String, password: String): Result<User> = 
                Result.success(User("1", email, "test", "Test User", null, null, null))
            override suspend fun register(e: String, p: String, u: String, f: String): Result<User> = Result.failure(Exception())
            override suspend fun logout() {}
            override suspend fun sendPasswordResetEmail(e: String): Result<Unit> = Result.success(Unit)
        }
        val useCase = LoginUseCase(repository)
        val viewModel = LoginViewModel(useCase)

        viewModel.onEvent(LoginUiEvent.OnEmailChanged("test@test.com"))
        viewModel.onEvent(LoginUiEvent.OnPasswordChanged("password"))
        viewModel.onEvent(LoginUiEvent.OnLoginClicked)

        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.success)
        assertEquals(null, viewModel.uiState.value.error)
    }

    @Test
    fun `login should update state with error on invalid credentials`() = runTest {
        val repository = object : AuthRepository {
            override fun getCurrentUser(): Flow<User?> = flowOf(null)
            override suspend fun isUserLoggedIn(): Boolean = false
            override suspend fun login(email: String, password: String): Result<User> = 
                Result.failure(Exception("Invalid email or password"))
            override suspend fun register(e: String, p: String, u: String, f: String): Result<User> = Result.failure(Exception())
            override suspend fun logout() {}
            override suspend fun sendPasswordResetEmail(e: String): Result<Unit> = Result.success(Unit)
        }
        val useCase = LoginUseCase(repository)
        val viewModel = LoginViewModel(useCase)

        viewModel.onEvent(LoginUiEvent.OnEmailChanged("test@test.com"))
        viewModel.onEvent(LoginUiEvent.OnPasswordChanged("wrong"))
        viewModel.onEvent(LoginUiEvent.OnLoginClicked)

        advanceUntilIdle()

        assertEquals("Invalid email or password", viewModel.uiState.value.error)
        assertEquals(false, viewModel.uiState.value.success)
    }
}
