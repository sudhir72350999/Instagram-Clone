package com.sudhirtheindian.instagramclone.feature.auth.domain.usecase

import com.sudhirtheindian.instagramclone.feature.auth.domain.model.User
import com.sudhirtheindian.instagramclone.feature.auth.domain.repository.AuthRepository

class RegisterUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String, username: String, fullName: String): Result<User> {
        if (email.isBlank() || password.isBlank() || username.isBlank() || fullName.isBlank()) {
            return Result.failure(Exception("All fields are required"))
        }
        return repository.register(email, password, username, fullName)
    }
}
