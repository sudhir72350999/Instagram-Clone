package com.sudhirtheindian.instagramclone.feature.splash

import com.sudhirtheindian.instagramclone.feature.auth.domain.repository.AuthRepository
import com.sudhirtheindian.instagramclone.feature.auth.domain.usecase.CheckAuthStateUseCase
import com.sudhirtheindian.instagramclone.feature.splash.presentation.SplashUiState
import com.sudhirtheindian.instagramclone.feature.splash.presentation.SplashViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import com.sudhirtheindian.instagramclone.feature.auth.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `initial state should be Idle`() = runTest {
        val repository = FakeAuthRepository(false)
        val useCase = CheckAuthStateUseCase(repository)
        val viewModel = SplashViewModel(useCase)

        assertEquals(SplashUiState.Idle, viewModel.uiState.value)
    }

    @Test
    fun `should navigate to Authenticated when user is logged in`() = runTest {
        val repository = FakeAuthRepository(true)
        val useCase = CheckAuthStateUseCase(repository)
        val viewModel = SplashViewModel(useCase)

        testDispatcher.scheduler.advanceTimeBy(2001) // Account for delay(2000)
        advanceUntilIdle()

        assertEquals(SplashUiState.Authenticated, viewModel.uiState.value)
    }

    @Test
    fun `should navigate to Unauthenticated when user is not logged in`() = runTest {
        val repository = FakeAuthRepository(false)
        val useCase = CheckAuthStateUseCase(repository)
        val viewModel = SplashViewModel(useCase)

        testDispatcher.scheduler.advanceTimeBy(2001)
        advanceUntilIdle()

        assertEquals(SplashUiState.Unauthenticated, viewModel.uiState.value)
    }
}

class FakeAuthRepository(private val loggedIn: Boolean) : AuthRepository {
    override fun getCurrentUser(): Flow<User?> = flowOf(null)
    override suspend fun isUserLoggedIn(): Boolean = loggedIn
    override suspend fun login(email: String, password: String): Result<User> = Result.failure(Exception("Not implemented"))
    override suspend fun register(email: String, password: String, username: String, fullName: String): Result<User> = Result.failure(Exception("Not implemented"))
    override suspend fun logout() {}
    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> = Result.failure(Exception("Not implemented"))
}
