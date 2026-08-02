package com.sudhirtheindian.instagramclone.feature.auth.domain.repository

import com.sudhirtheindian.instagramclone.feature.auth.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getCurrentUser(): Flow<User?>
    suspend fun isUserLoggedIn(): Boolean
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(email: String, password: String, username: String, fullName: String): Result<User>
    suspend fun logout()
    suspend fun sendPasswordResetEmail(email: String): Result<Unit>
}
