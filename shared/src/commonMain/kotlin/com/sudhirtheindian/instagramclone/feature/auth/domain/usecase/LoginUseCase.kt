package com.sudhirtheindian.instagramclone.feature.auth.domain.usecase

import com.sudhirtheindian.instagramclone.feature.auth.domain.model.User
import com.sudhirtheindian.instagramclone.feature.auth.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(Exception("Email and password cannot be empty"))
        }
        return repository.login(email, password)
    }
}
